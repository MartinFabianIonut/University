using System;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Runtime.Serialization.Formatters.Binary;
using System.Runtime.Serialization;
using System.Threading;
using SeventhWeekCSharpServerClient.services;
using SeventhWeekCSharpServerClient.domain;
using log4net;

namespace SeventhWeekCSharpServerClient.networking
{

    public class ServicesRpcProxy : IServices
    {
        private static readonly ILog Logger = LogManager.GetLogger("ServicesRpcProxy");
        private readonly string host;
        private readonly int port;
        private IObserver employeeObserver;
        private NetworkStream stream;
        private IFormatter formatter;
        private TcpClient connection;
        private readonly Queue<Response> responses;
        private volatile bool finished;
        private EventWaitHandle waitHandle;
        
        public ServicesRpcProxy(string host, int port)
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
                formatter.Serialize(stream, request);
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
            if (response.Type != ResponseType.TICKET_SOLD) return;
            var ticket = (Ticket<int>)response.Data;
            Logger.Info("Ticket sold " + ticket);
            try
            {
                employeeObserver.TicketAdded(ticket);
            }
            catch (MyException e)
            {
                Logger.Error("Error handle update: " + e);
            }
        }

        private static bool IsUpdate(Response response)
        {
            return response.Type == ResponseType.TICKET_SOLD;
        }
        
        public Employee<int> AuthenticateEmployee(Employee<int> employee, IObserver employeeObserver)
        {
            InitializeConnection();
            var req = new Request.Builder().Type(RequestType.LOGIN).Data(employee).Build();
            SendRequest(req);
            var response = ReadResponse();
            switch (response.Type)
            {
                case ResponseType.OK:
                    this.employeeObserver = employeeObserver;
                    Logger.Info("Logged in");
                    return (Employee<int>)response.Data;
                case ResponseType.ERROR:
                {
                    Logger.Error("Error logging in" + response.Data.ToString());
                    var err = response.Data.ToString();
                    CloseConnection();
                    throw new MyException(err);
                }
                default:
                    return null;
            }
        }

        public void Logout(Employee<int> employee)
        {
            var req = new Request.Builder().Type(RequestType.LOGOUT).Data(employee).Build();
            SendRequest(req);
            var response = ReadResponse();
            CloseConnection();
            if (response.Type == ResponseType.ERROR)
            {
                Logger.Error("Error logging out" + response.Data);
                var err = response.Data.ToString();
                throw new MyException(err);
            }
            Logger.Info("Logged out");
        }

        public void AddTickets(Ticket<int> ticket)
        {
            var req = new Request.Builder().Type(RequestType.BUY_TICKET).Data(ticket).Build();
            SendRequest(req);
            var response = ReadResponse();
            if (response.Type == ResponseType.ERROR)
            {
                Logger.Error("Error adding ticket" + response.Data.ToString());
                var err = response.Data.ToString();
                throw new MyException(err);
            }
            Logger.Info("Ticket added");
            Console.WriteLine("Ticket added");
        }

        public List<ShowDTO<int>> GetAllShowsDTO()
        {
            var req = new Request.Builder().Type(RequestType.GET_ALL_SHOWS).Data(null).Build();
            SendRequest(req);
            var response = ReadResponse();
            if (response.Type == ResponseType.ERROR)
            {
                Logger.Error("Error getting shows" + response.Data.ToString());
                Console.WriteLine("Error getting shows" + response.Data.ToString());
                var err = response.Data.ToString();
                throw new MyException(err);
            }
            Logger.Info("Got shows");
            Console.WriteLine("Got shows");
            return (List<ShowDTO<int>>)response.Data;
        }

        public List<Artist<int>> GetAllArtists()
        {
            var req = new Request.Builder().Type(RequestType.GET_ALL_ARTISTS).Data(null).Build();
            SendRequest(req);
            var response = ReadResponse();
            if (response.Type == ResponseType.ERROR)
            {
                Logger.Error("Error getting artists" + response.Data.ToString());
                var err = response.Data.ToString();
                throw new MyException(err);
            }
            Logger.Info("Got artists");
            return (List<Artist<int>>)response.Data;
        }

        public Artist<int> FindArtist(int id)
        {
            var req = new Request.Builder().Type(RequestType.FIND_ARTIST).Data(id).Build();
            SendRequest(req);
            var response = ReadResponse();
            if (response.Type == ResponseType.ERROR)
            {
                Logger.Error("Error finding artist" + response.Data.ToString());
                var err = response.Data.ToString();
                throw new MyException(err);
            }
            Logger.Info("Found artist");
            return (Artist<int>)response.Data;
        }

        protected virtual void Run()
        {
            while (!finished)
            {
                try
                {
                    var response = formatter.Deserialize(stream);
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