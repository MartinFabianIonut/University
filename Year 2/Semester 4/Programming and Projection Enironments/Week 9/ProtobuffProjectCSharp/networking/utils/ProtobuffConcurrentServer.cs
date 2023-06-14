using log4net.Repository.Hierarchy;
using log4net;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using ProtobuffNetworking;
using ProtobuffProject.services;

namespace ProtobuffProject.networking.utils
{
    public class ProtobuffConcurrentServer : AbsConcurrentServer
    {
        private readonly IServices service;
        private static readonly ILog logger = LogManager.GetLogger("RpcConcurrentServer");
      
        private ProtoWorker worker;
        public ProtobuffConcurrentServer(string host, int port, IServices service) : base(host, port)
        {
            this.service = service; 
            logger.Info("ProtobuffConcurrentServer created");
            Console.WriteLine(@"ProtobuffConcurrentServer created");
        }
        protected override Thread CreateWorker(TcpClient client)
        {
            worker = new ProtoWorker(service, client);
            return new Thread(new ThreadStart(worker.Run));
        }

    }
}
