using System;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Runtime.Serialization.Formatters.Binary;
using System.Runtime.Serialization;
using System.Threading;
using Google.Protobuf;
using ProtobuffProject.services;
using ProtobuffProject.domain;
using GRPCProject.Protocol;

namespace ProtobuffNetworking
{
    public class ProtoProxy : IServices
    {
        private readonly string host;
        private readonly int port;
        private IObserver employeeObserver;
        private NetworkStream stream;
        private IFormatter formatter;
        private TcpClient connection;
        private readonly Queue<Response> responses;
        private volatile bool finished;
        private EventWaitHandle waitHandle;
        
        public ProtoProxy(string host, int port)
        {
            this.host = host;
            this.port = port;
            responses = new Queue<Response>();
        }

        private void CloseConnection()
        {
            finished = true;
            try
            {
                stream.Close();
                connection.Close();
                waitHandle.Close();
                employeeObserver = null;
                Console.WriteLine("Connection closed!");
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }

        private void SendRequest(Request request)
        {
            try
            {
                request.WriteDelimitedTo(stream);
                stream.Flush();
            }
            catch (Exception e)
            {
                throw new MyException("Error sending object " + e);
            }
        }

        private Response ReadResponse()
        {
            Response response = null;
            try
            {
                waitHandle.WaitOne();
                lock (responses)
                {
                    response = responses.Dequeue();
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
            return response;
        }
        
        private void InitializeConnection()
        {
            try
            {
                connection = new TcpClient(host, port);
                stream = connection.GetStream();
                formatter = new BinaryFormatter();
                finished = false;
                waitHandle = new AutoResetEvent(false);
                StartReader();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }
        
        private void StartReader()
        {
            var tw = new Thread(Run);
            tw.Start();
        }

        private void HandleUpdate(Response response)
        {
            if (response.Type != Response.Types.Type.TicketSold) return;
            var ticket = ProtoUtils.GetTicket(response);
            Console.WriteLine("Ticket sold " + ticket);
            try
            {
                employeeObserver.TicketAdded(ticket);
            }
            catch (MyException e)
            {
                Console.WriteLine("Error handle update: " + e);
            }
        }

        private static bool IsUpdate(Response response)
        {
            return response.Type == Response.Types.Type.TicketSold;
        }
        
        public Employee<int> AuthenticateEmployee(Employee<int> employee, IObserver employeeObserver)
        {
            InitializeConnection();
            Console.WriteLine("Login request ...");
            var req = ProtoUtils.CreateLoginRequest(employee);
            SendRequest(req);
            var response = ReadResponse();
            switch (response.Type)
            {
                case Response.Types.Type.Ok:
                    this.employeeObserver = employeeObserver;
                    Console.WriteLine("Logged in");
                    return ProtoUtils.GetEmployee(response);
                case Response.Types.Type.Error:
                {
                    var err = ProtoUtils.GetError(response);
                    Console.WriteLine("Error logging in" + err);
                    CloseConnection();
                    throw new MyException(err);
                }
                default:
                    return null;
            }
        }

        public void Logout(Employee<int> employee)
        {
            var req = ProtoUtils.CreateLogoutRequest(employee);
            SendRequest(req);
            var response = ReadResponse();
            CloseConnection();
            if (response.Type == Response.Types.Type.Error)
            {
                var err = ProtoUtils.GetError(response);
                Console.WriteLine("Error logging out" + err);
                throw new MyException(err);
            }
            Console.WriteLine("Logged out");
        }

        public void AddTickets(Ticket<int> ticket)
        {
            var req = ProtoUtils.CreateAddTicketRequest(ticket);
            SendRequest(req);
            var response = ReadResponse();
            if (response.Type == Response.Types.Type.Error)
            {
                var err = ProtoUtils.GetError(response);
                Console.WriteLine("Error adding ticket" + err);
                throw new MyException(err);
            }
            Console.WriteLine("Ticket added");
        }

        public List<ShowDTO<int>> GetAllShowsDTO()
        {
            var req = ProtoUtils.CreateGetAllShowsDTORequest();
            SendRequest(req);
            var response = ReadResponse();
            if (response.Type == Response.Types.Type.Error)
            {
                var err = ProtoUtils.GetError(response);
                Console.WriteLine("Error getting shows" + err);
                throw new MyException(err);
            }
            Console.WriteLine("Got shows");
            return ProtoUtils.GetShows(response);
        }

        public List<Artist<int>> GetAllArtists()
        {
            var req = ProtoUtils.CreateGetAllArtistsRequest();
            SendRequest(req);
            var response = ReadResponse();
            if (response.Type == Response.Types.Type.Error)
            {
                var err = ProtoUtils.GetError(response);
                Console.WriteLine("Error getting artists" + err);
                throw new MyException(err);
            }
            Console.WriteLine("Got artists");
            return ProtoUtils.GetArtists(response);
        }

        public Artist<int> FindArtist(int id)
        {
            var req = ProtoUtils.CreateFindArtistRequest(id);
            SendRequest(req);
            var response = ReadResponse();
            if (response.Type == Response.Types.Type.Error)
            {
                var err = ProtoUtils.GetError(response);
                Console.WriteLine("Error finding artist" + err);
                throw new MyException(err);
            }
            Console.WriteLine("Found artist");
            return ProtoUtils.GetArtist(response);
        }

        protected virtual void Run()
        {
            while (!finished)
            {
                try
                {
                    var response = Response.Parser.ParseDelimitedFrom(stream);
                    Console.WriteLine("response received " + response);
                    if (IsUpdate((Response) response))
                    {
                        HandleUpdate((Response) response);
                    }
                    else
                    {
                        lock (responses)
                        {
                            responses.Enqueue((Response)response);
                        }
                        waitHandle.Set();
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine("Reading error " + e);
                }
            }
        }
    }
}