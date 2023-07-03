using ASaseaSaptamana.repository;
using ASaseaSaptamana.repository.interfaces;
using ASaseaSaptamana.service;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace ASaseaSaptamana
{
    internal static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]

        static string GetConnectionStringByName(string name)
        {
            // Assume failure.
            string returnValue = null;

            // Look for the name in the connectionStrings section.
            var settings = ConfigurationManager.ConnectionStrings[name];

            // If found, return the connection string.
            if (settings != null)
                returnValue = settings.ConnectionString;

            return returnValue;
        }

        static void Main()
        {
            IDictionary<string, string> props = new SortedList<string, string>();
            props.Add("ConnectionString", GetConnectionStringByName("shows"));
            IArtistRepository artistRepository = new ArtistDBRepository(props);
            IEmployeeRepository employeeRepository = new EmployeeDBRepository(props);
            IShowRepository showRepository = new ShowDBRepository(props);
            ITicketRepository ticketRepository = new TicketDBRepository(props);
            Service service = new Service(artistRepository, employeeRepository, showRepository, ticketRepository);

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new Login(service));
        }
    }
}
