using SeventhWeekCSharpServerClient.domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;

namespace SeventhWeekCSharpServerClient.client
{
    public partial class MainStage : Form
    {
        private readonly ClientController clientController;
        public MainStage(ClientController clientController)
        {
            InitializeComponent();
            this.clientController = clientController;
            InitialiseList();
            clientController.UpdateEvent += UserUpdate;
        }
        
        private IList<ShowDTO<int>> showList;
        private IList<ShowDTO<int>> showFilteredList;

        private void InitialiseList()
        {
            showList = clientController.GetAllShowsDTO().ToList();
            allShowsDataGridView.DataSource = showList;
            allShowsDataGridView.Columns["Id"].Visible = false;
            filteredShowsDataGridView.DataSource = showFilteredList;
        }

        private void UserUpdate(object sender, EmployeeEventArgs e)
        {
            if (e.EmployeeEventType != EmployeeEvent.TICKET_BOUGHT) return;
            var ticket = (Ticket<int>)e.Data;
            foreach (var show in showList)
            {
                if (show.Id != ticket.IdShow) continue;
                show.Available-=ticket.NoOfSeats;
                show.Unavailable+=ticket.NoOfSeats;
            }
            allShowsDataGridView.BeginInvoke(new UpdateListBoxCallback(UpdateListBox), new Object[] { allShowsDataGridView, showList });
        }

        //for updating the GUI

        //1. define a method for updating the ListBox
        private static void UpdateListBox(DataGridView gridView, IList<ShowDTO<int>> newData)
        {
            gridView.DataSource = null;
            gridView.DataSource = newData;
            gridView.Columns["Id"].Visible = false;
        }

        //2. define a delegate to be called back by the GUI Thread
        private delegate void UpdateListBoxCallback(DataGridView list, IList<ShowDTO<int>> data);



        private void MainStage_Load(object sender, EventArgs e)
        {
            dateTimePicker1.Format = DateTimePickerFormat.Custom;
            dateTimePicker1.CustomFormat = @"dd.MM.yyyy";
            this.Text = @"Logged as " + clientController.Employee;
        }

        private void dateTimePicker1_ValueChanged(object sender, EventArgs e)
        {
            var date = dateTimePicker1.Text;
            var unfiltered = clientController.GetAllShowsDTO().ToList();
            showFilteredList = unfiltered.Where(x => x.Date == date).ToList();
            filteredShowsDataGridView.DataSource = showFilteredList;
            filteredShowsDataGridView.Columns["Id"].Visible = false;
        }

        private void allShowsDataGridView_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            if (allShowsDataGridView.CurrentCell == null) return;
            var rowIndex = allShowsDataGridView.CurrentCell.RowIndex;
            var showDTO = allShowsDataGridView.Rows[rowIndex].DataBoundItem as ShowDTO<int>;
            if (showDTO == null) return;
            var buy = new Buy(clientController, showDTO);
            buy.Text = @"Buying tickets for " + showDTO.Title + " at " + showDTO.Place + " in " + showDTO.Date;
            buy.ShowDialog();
        }

        private void refreshButton_Click(object sender, EventArgs e)
        {
            InitialiseList();
        }

        private void logoutButton_Click(object sender, EventArgs e)
        {
            this.Hide();
            clientController.Logout();
            clientController.UpdateEvent -= UserUpdate;
            var login = new Login(new ClientController(clientController.Service));
            login.ShowDialog();
            this.Close();
        }

        private void MainStage_FormClosing(object sender, FormClosingEventArgs e)
        {
            clientController.Logout();
            clientController.UpdateEvent -= UserUpdate;
        }
    }
}
