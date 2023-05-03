using ProtobuffProject.networking;
using ProtobuffProject.services;
using System;
using System.Windows.Forms;
using ProtobuffNetworking;

namespace ProtobuffProject.client
{
    internal static class ProgramProto
    {
        [STAThread]
        private static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);      
            IServices service = new ProtoProxy("127.0.0.1", 55556);
            var ctrl = new ClientController(service);
            var win = new Login(ctrl);
            Application.Run(win);
        }
    }
}
