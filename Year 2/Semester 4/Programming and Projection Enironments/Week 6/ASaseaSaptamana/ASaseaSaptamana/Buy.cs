using ASaseaSaptamana.domain;
using ASaseaSaptamana.service;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace ASaseaSaptamana
{
    public partial class Buy : Form
    {
        private Service service;
        private ShowDTO<int> showDTO;
        public Buy(Service service, ShowDTO<int> showDTO)
        {
            InitializeComponent();
            this.service = service;
            this.showDTO = showDTO;
        }

        private void buyButton_Click(object sender, EventArgs e)
        {
            if (numberOfSeatsTextBox.Text.Length > 0 && nameTextBox.Text.Length > 0)
            {
                try
                {
                    var noOfSeats = int.Parse(numberOfSeatsTextBox.Text);
                    if (showDTO.Available > noOfSeats)
                    {
                        for (int i = 1; i <= noOfSeats; i++)
                            service.AddTicket(new Ticket<int>(1, showDTO.Id, nameTextBox.Text));
                        this.Close();
                    }
                    else
                        MessageBox.Show("There are not enough available seats!");
                }
                catch
                {
                    MessageBox.Show("The input from the number of seats is not an integer!");
                }
            }
            else
            {
                MessageBox.Show("Fill all!");
            }
        }
    }
}
