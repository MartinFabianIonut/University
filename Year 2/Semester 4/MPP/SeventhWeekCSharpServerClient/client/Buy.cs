using SeventhWeekCSharpServerClient.domain;
using System;
using System.Windows.Forms;

namespace SeventhWeekCSharpServerClient.client
{
    public partial class Buy : Form
    {
        private readonly ClientController clientController;
        private readonly ShowDTO<int> showDTO;
        public Buy(ClientController clientController, ShowDTO<int> showDTO)
        {
            InitializeComponent();
            this.clientController = clientController;
            this.showDTO = showDTO;
        }

        private void buyButton_Click(object sender, EventArgs e)
        {
            if (numberOfSeatsTextBox.Text.Length > 0 && nameTextBox.Text.Length > 0)
            {
                try
                {
                    var noOfSeats = int.Parse(numberOfSeatsTextBox.Text);
                    if (showDTO.Available >= noOfSeats)
                    {
                        clientController.AddTickets(new Ticket<int>(1, showDTO.Id, nameTextBox.Text, noOfSeats));
                        this.Close();
                    }
                    else
                        MessageBox.Show(@"There are not enough available seats!");
                }
                catch
                {
                    MessageBox.Show(@"The input from the number of seats is not an integer!");
                }
            }
            else
            {
                MessageBox.Show(@"Fill all!");
            }
        }
    }
}
