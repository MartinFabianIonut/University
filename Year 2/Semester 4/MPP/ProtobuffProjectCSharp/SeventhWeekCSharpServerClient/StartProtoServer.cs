using System;
using System.Collections.Generic;
using System.Configuration;
using ProtobuffProject.repository;
using ProtobuffProject.repository.interfaces;
using ProtobuffProject.services;
using ProtobuffProject.networking.utils;

namespace ProtobuffProject.server
{
    internal static class StartProtoServer
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
            var server = new ProtobuffConcurrentServer("127.0.0.1", 55556, service);
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
