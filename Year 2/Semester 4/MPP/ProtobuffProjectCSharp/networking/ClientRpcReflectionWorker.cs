using System;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Runtime.Serialization.Formatters.Binary;
using System.Runtime.Serialization;
using System.Threading;
using ProtobuffProject.services;
using ProtobuffProject.domain;
using System.Reflection;
using log4net;
using System.IO;

namespace ProtobuffProject.networking
{
    public class ClientRpcReflectionWorker : IObserver //, Runnable
    {

        private static readonly ILog Logger = LogManager.GetLogger("ClientRpcReflectionWorker");

        private readonly IServices service;
        private readonly TcpClient connection;
        private readonly NetworkStream stream;
        private readonly IFormatter formatter;
        private volatile bool connected;
        public ClientRpcReflectionWorker(IServices service, TcpClient connection)
        {
            this.service = service;
            this.connection = connection;
            try
            {
                stream = connection.GetStream();
                formatter = new BinaryFormatter();
                connected = true;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }

        public virtual void Run()
        {
            while (connected)
            {
                try
                {
                    var request = formatter.Deserialize(stream);
                    object response = HandleRequest((Request)request);
                    if (response != null)
                    {
                        SendResponse((Response)response);
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.StackTrace);
                }

                try
                {
                    Thread.Sleep(1000);
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.StackTrace);
                }
            }
            try
            {
                stream.Close();
                connection.Close();
            }
            catch (Exception e)
            {
                Console.WriteLine("Error " + e);
            }
        }

        private Response HandleRequest(Request request)
        {       
            Response response = null;
            var handlerName = "Handle" + request.Type;
            Console.WriteLine("HandlerName " + handlerName);
            try
            {
                var method = this.GetType().GetMethod(handlerName, new[] { typeof(Request) });
                response = (Response)method.Invoke(this, new[] { request });
                Console.WriteLine("Method " + handlerName + " invoked");
            }
            catch (TargetInvocationException ex)    
            {
                Console.WriteLine(ex.InnerException);
            }
            catch (AmbiguousMatchException ex)
            {
                Console.WriteLine(ex.Message);
            }
            catch (TargetParameterCountException ex)
            {
                Console.WriteLine(ex.Message);
            }
            catch (TargetException ex)
            {
                Console.WriteLine(ex.Message);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
            return response;
        }

        public Response HandleLOGIN(Request request)
        {
            Logger.InfoFormat("method entered: handleLOGIN with parameters {0}", request);
            var employee = (Employee<int>)request.Data;
            try
            {
                var foundEmployee = service.AuthenticateEmployee(employee, this);
                Logger.Info("Employee logged in");
                return new Response.Builder().Type(ResponseType.OK).Data(foundEmployee).Build();
            }
            catch (MyException e)
            {
                connected = false;
                Logger.Error("Error in worker (solving method handleLOGIN): {0}", e);
                return new Response.Builder().Type(ResponseType.ERROR).Data(e.Message).Build();
            }
        }

        public Response HandleBUY_TICKET(Request request)
        {
            Logger.InfoFormat("method entered: handleBUY_TICKET with parameters {0}", request);
            var ticket = (Ticket<int>)request.Data;
            try
            {
                service.AddTickets(ticket);
                Logger.Info("Ticket bought");
                return new Response.Builder().Type(ResponseType.OK).Data(ticket).Build();
            }
            catch (MyException e)
            {
                Logger.Error("Error in worker (solving method handleBUY_TICKET): {0}", e);
                return new Response.Builder().Type(ResponseType.ERROR).Data(e.Message).Build();
            }
        }

        public Response HandleLOGOUT(Request request)
        {
            Logger.InfoFormat("method entered: handleLOGOUT with parameters {0}", request);
            var employee = (Employee<int>)request.Data;
            try
            {
                service.Logout(employee);
                connected = false;
                Logger.Info("User logged out");
                return new Response.Builder().Type(ResponseType.OK).Build(); ;
            }
            catch (MyException e)
            {
                Logger.Error("Error in worker (solving method handleLOGOUT): {0}", e);
                return new Response.Builder().Type(ResponseType.ERROR).Data(e.Message).Build();
            }
        }

        public Response HandleGET_ALL_SHOWS(Request request)
        {
            Logger.InfoFormat("method entered: handleGET_ALL_SHOWS with parameters {0}", request);
            Console.WriteLine("HandleGetAllShows");
            try
            {
                var showsDTO = (List<ShowDTO<int>>)service.GetAllShowsDTO();
                Logger.InfoFormat("Shows found {0}", showsDTO);
                Console.WriteLine(@"Shows found {0}", showsDTO);
                return new Response.Builder().Type(ResponseType.GET_ALL_SHOWS).Data(showsDTO).Build();
            }
            catch (MyException e)
            {
                Logger.Error("Error in worker (solving method handleGET_ALL_SHOWS): {0}", e);
                Console.WriteLine(@"Error in worker (solving method handleGET_ALL_SHOWS): {0}", e);
                return new Response.Builder().Type(ResponseType.ERROR).Data(e.Message).Build();
            }
        }

        public Response HandleGET_ALL_ARTISTS(Request request)
        {
            Logger.InfoFormat("method entered: handleGET_ALL_ARTISTS with parameters {0}", request);
            try
            {
                var artists = (List<Artist<int>>)service.GetAllArtists();
                Logger.InfoFormat("Artists found {0}", artists);
                return new Response.Builder().Type(ResponseType.GET_ALL_ARTISTS).Data(artists).Build();
            }
            catch (MyException e)
            {
                Logger.Error("Error in worker (solving method handleGET_ALL_ARTISTS): {0}", e);
                return new Response.Builder().Type(ResponseType.ERROR).Data(e.Message).Build();
            }
        }

        public Response HandleFIND_ARTIST(Request request)
        {
            Logger.InfoFormat("method entered: handleFIND_ARTIST with parameters {0}", request);
            Console.WriteLine("method entered: handleFIND_ARTIST with parameters " + request);
            var idArtist = (int)request.Data;
            try
            {
                var artist = service.FindArtist(idArtist);
                Logger.InfoFormat("Artist found {0}", artist);
                Console.WriteLine("Artist found " + artist);
                return new Response.Builder().Type(ResponseType.FIND_ARTIST).Data(artist).Build();
            }
            catch (MyException e)
            {
                Logger.Error("Error in worker (solving method handleFIND_ARTIST): {0}", e);
                Console.WriteLine("Error in worker (solving method handleFIND_ARTIST): " + e);
                return new Response.Builder().Type(ResponseType.ERROR).Data(e.Message).Build();          
            }    
        }

        private void SendResponse(Response response)
        {
            Console.WriteLine("sending response " + response);
            lock (stream)
            {
                formatter.Serialize(stream, response);
                stream.Flush();
            }
        }

        public void TicketAdded(Ticket<int> ticket)
        {
            var resp = new Response.Builder().Type(ResponseType.TICKET_SOLD).Data(ticket).Build();
            Logger.Info($"Ticket sold {ticket}");
            Console.WriteLine($@"Ticket sold {ticket}");
            try
            {
                SendResponse(resp);
                Logger.Info("Response sent");
                Console.WriteLine("Response sent");
            }
            catch (IOException e)
            {
                Console.WriteLine(e);
                Logger.Error($"Error in worker (sending response): {e}");
                Console.WriteLine(@"Error in worker (sending response): {0}", e);
            }
        }
    }

}