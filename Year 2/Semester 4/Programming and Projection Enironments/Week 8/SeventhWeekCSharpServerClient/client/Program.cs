using SeventhWeekCSharpServerClient.networking;
using SeventhWeekCSharpServerClient.services;
using System;
using System.Windows.Forms;

namespace SeventhWeekCSharpServerClient.client
{
    internal static class Program
    {
        [STAThread]
        private static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);      
            IServices service = new ServicesRpcProxy("127.0.0.1", 55556);
            var ctrl = new ClientController(service);
            var win = new Login(ctrl);
            Application.Run(win);
        }
    }
}
