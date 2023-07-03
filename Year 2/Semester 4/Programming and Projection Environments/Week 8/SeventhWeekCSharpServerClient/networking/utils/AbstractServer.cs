using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using log4net.Repository.Hierarchy;
using System.IO;
using log4net;
using log4net.Util;

namespace SeventhWeekCSharpServerClient.networking.utils
{
    public abstract class AbstractServer
    {
        private static readonly ILog logger = LogManager.GetLogger("AbstractServer");
        private TcpListener server;
        private string host;
        private int port;
        public AbstractServer(string host, int port)
        {
            this.host = host;
            this.port = port;
        }
        public void Start()
        {
            IPAddress adr = IPAddress.Parse(host);
            IPEndPoint ep = new IPEndPoint(adr, port);
            try
            {
                server = new TcpListener(ep);
                server.Start();
                while (true)
                {
                    logger.Info("Waiting for clients ...");
                    TcpClient client = server.AcceptTcpClient();
                    logger.Info("Client connected ...");
                    ProcessRequest(client);
                }
            }
            catch (IOException e)
            {
                logger.Error("Error starting server", e);
                throw new ServerException("Starting server error ", e);
            }
        }

        public abstract void ProcessRequest(TcpClient client);

        public void Stop()
        {
            try
            {
                server.Stop();
                logger.Info("Server stopped");
            }
            catch (IOException e)
            {
                logger.Error("Error stopping server", e);
                throw new ServerException("Closing server error ", e);
            }
        }
    }
}
