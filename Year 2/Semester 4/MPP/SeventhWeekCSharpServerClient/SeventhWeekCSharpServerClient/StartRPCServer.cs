using System;
using System.Collections.Generic;
using System.Configuration;
using SeventhWeekCSharpServerClient.repository;
using SeventhWeekCSharpServerClient.repository.interfaces;
using SeventhWeekCSharpServerClient.services;
using SeventhWeekCSharpServerClient.networking.utils;

namespace SeventhWeekCSharpServerClient.server
{
    internal static class StartRPCServer
    {
        [STAThread]
        private static string GetConnectionStringByName(string name)
        {
            string returnValue = null;
            var settings = ConfigurationManager.ConnectionStrings[name];
            if (settings != null)
                returnValue = settings.ConnectionString;
            return returnValue;
        }

        private static void Main(string[] args)
        {

            IDictionary<string, string> props = new SortedList<string, string>();
            props.Add("ConnectionString", GetConnectionStringByName("shows"));
            IArtistRepository artistRepository = new ArtistDBRepository(props);
            IEmployeeRepository employeeRepository = new EmployeeDBRepository(props);
            IShowRepository showRepository = new ShowDBRepository(props);
            ITicketRepository ticketRepository = new TicketDBRepository(props);
            IServices service = new ServicesImpl(artistRepository, employeeRepository, showRepository, ticketRepository);
            var server = new RpcConcurrentServer("127.0.0.1", 55556, service);
            try
            {
                server.Start();
                Console.WriteLine(@"Server started ...");
                Console.ReadLine();
            }
            catch (ServerException e)
            {
                Console.Error.WriteLine("Error starting the server" + e.Message);
            }
            finally
            {
                try
                {
                    server.Stop();
                }
                catch (ServerException e)
                {
                    Console.Error.WriteLine("Error stopping server " + e.Message);
                }
            }
            
        }
    }

}
