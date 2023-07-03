using ASaseaSaptamana.domain;
using ASaseaSaptamana.repository;
using ASaseaSaptamana.service;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Configuration;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace ASaseaSaptamana
{
    public partial class Login : Form
    {
        private Service service;

        public Login(Service service)
        {
            InitializeComponent();
            this.service = service;
            //TestMethod();
        }

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

        private void TestMethod()
        {
            IDictionary<string, string> props = new SortedList<string, string>();
            props.Add("ConnectionString", GetConnectionStringByName("shows"));

            /*MessageBox.Show(GetConnectionStringByName("shows"));
            ArtistDBRepository artistRepository = new ArtistDBRepository(props);
            MessageBox.Show(artistRepository.GetAll().Count.ToString());
            MessageBox.Show(artistRepository.GetAll().Count.ToString());
            artistRepository.Add(new Artist<int>(0, "Ionel", "Marinesecu"));
            MessageBox.Show(artistRepository.GetAll().Count.ToString());
            artistRepository.Delete(3);
            MessageBox.Show(artistRepository.GetAll().Count.ToString());
            MessageBox.Show(artistRepository.FindById(12).ToString());*/

            ShowDBRepository showDBRepository = new ShowDBRepository(props);
            //showDBRepository.Add(new Show<int>(1,"The Greatest Show", DateOnly.FromDateTime(DateTime.Now), "Cluj-Napoca", 1));
            List<Show<int>> list = showDBRepository.GetAll();
            list.ForEach(x => { MessageBox.Show(x.ToString()); });
        }

        private void Login_Load(object sender, EventArgs e)
        {

        }

        private void loginButton_Click(object sender, EventArgs e)
        {
            if (usernameTextBox.Text.Length > 0 && passwordTextBox.Text.Length > 0)
            {
                string username = usernameTextBox.Text.ToString();
                string password = passwordTextBox.Text.ToString();
                Employee<int> employee = service.AuthenticateEmployee(username, password);
                if (employee != null)
                {
                    this.Hide();
                    MainStage mainStage = new MainStage(service, employee);
                    mainStage.ShowDialog();
                    this.Close();

                }
                else
                {
                    MessageBox.Show("Authentication failed!");
                }
            }
        }
    }
}
