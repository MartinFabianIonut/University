using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
namespace SeventhWeekCSharpServerClient.networking.utils
{
    public abstract class AbsConcurrentServer: AbstractServer
    {
        public AbsConcurrentServer(string host, int port) : base(host, port)
        { }

        public override void ProcessRequest(TcpClient client)
        {
            Thread t = CreateWorker(client);
            t.Start();
        }

        protected abstract Thread CreateWorker(TcpClient client);
    }
}
