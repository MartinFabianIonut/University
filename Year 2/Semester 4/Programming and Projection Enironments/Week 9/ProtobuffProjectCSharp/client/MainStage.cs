using ProtobuffProject.domain;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Windows.Forms;

namespace ProtobuffProject.client
{
    public partial class MainStage : Form
    {
        private readonly ClientController clientController;
        private readonly Login login;
        public MainStage(ClientController clientController, Login login)
        {
            InitializeComponent();
            this.clientController = clientController;
            this.login = login;
            InitialiseList();
            clientController.UpdateEvent += UserUpdate;
        }
        
        private IList<ShowDTO<int>> showList;
        private IList<ShowDTO<int>> showFilteredList;

        private void InitialiseList()
        {
            showList = clientController.GetAllShowsDTO().ToList();
            allShowsDataGridView.DataSource = showList;
            if(allShowsDataGridView.Columns["Id"] != null)
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
            allShowsDataGridView.BeginInvoke(new UpdateListBoxCallback(UpdateDataGridView), new Object[] { allShowsDataGridView, showList });
        }

        //for updating the GUI

        //1. define a method for updating the DataGridView
        private static void UpdateDataGridView(DataGridView gridView, IList<ShowDTO<int>> newData)
        {
            gridView.DataSource = null;
            gridView.DataSource = newData;
            if(gridView.Columns["Id"] != null)
                gridView.Columns["Id"].Visible = false;
        }

        //2. define a delegate to be called back by the GUI Thread
        private delegate void UpdateListBoxCallback(DataGridView list, IList<ShowDTO<int>> data);



        private void MainStage_Load(object sender, EventArgs e)
        {
            dateTimePicker1.Format = DateTimePickerFormat.Custom;
            dateTimePicker1.CustomFormat = @"yyyy-MM-dd";
            this.Text = @"Logged as " + clientController.Employee;
        }

        private void dateTimePicker1_ValueChanged(object sender, EventArgs e)
        {
            var date = dateTimePicker1.Text;
            var unfiltered = clientController.GetAllShowsDTO().ToList();
            showFilteredList = unfiltered.Where(x => x.Date == date).ToList();
            filteredShowsDataGridView.DataSource = showFilteredList;
            if (filteredShowsDataGridView.Columns["Id"] != null)
                filteredShowsDataGridView.Columns["Id"].Visible = false;
        }

        private void allShowsDataGridView_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            if (allShowsDataGridView.CurrentCell == null) return;
            var rowIndex = allShowsDataGridView.CurrentCell.RowIndex;
            var style = allShowsDataGridView.Rows[rowIndex].DefaultCellStyle;
            var bgColor = style.BackColor;
            if (bgColor == Color.LightCoral)
            {
                MessageBox.Show(@"There are no available seats!");
                return;
            };
            var showDTO = allShowsDataGridView.Rows[rowIndex].DataBoundItem as ShowDTO<int>;
            if (showDTO == null) return;
            var buy = new Buy(clientController, showDTO);
            buy.Text = @"Buying tickets for " + showDTO.Title + " at " + showDTO.Place + " in " + showDTO.Date;
            buy.ShowDialog();
        }

        private void logoutButton_Click(object sender, EventArgs e)
        {
            this.Hide();
            clientController.Logout();
            clientController.UpdateEvent -= UserUpdate;
            login.ShowDialog();
            this.Close();
        }

        private void MainStage_FormClosing(object sender, FormClosingEventArgs e)
        {
            logoutButton_Click(sender, e);
        }

        private void allShowsDataGridView_CellFormatting(object sender, DataGridViewCellFormattingEventArgs e)
        {
            foreach (DataGridViewRow row in allShowsDataGridView.Rows)
            {
                if (Convert.ToInt32(row.Cells["Available"].Value) == 0)
                {
                    row.DefaultCellStyle.BackColor = Color.LightCoral;
                }
                if (Convert.ToInt32(row.Cells["Available"].Value) != 0)
                {
                    row.DefaultCellStyle.BackColor = Color.PaleGreen;
                }
            }
        }

        private void filteredShowsDataGridView_CellFormatting(object sender, DataGridViewCellFormattingEventArgs e)
        {
            foreach (DataGridViewRow row in filteredShowsDataGridView.Rows)
            {
                if (Convert.ToInt32(row.Cells["Available"].Value) == 0)
                {
                    row.DefaultCellStyle.BackColor = Color.LightCoral;
                }
                if (Convert.ToInt32(row.Cells["Available"].Value) != 0)
                {
                    row.DefaultCellStyle.BackColor = Color.PaleGreen;
                }
            }
        }
    }
}
