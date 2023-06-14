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
    public partial class MainStage : Form
    {
        
        public MainStage(Service service, Employee<int> employee)
        {
            InitializeComponent();
            this.service = service;
            this.employee = employee;
            InitialiseList();
        }
        
        private Service service;
        private Employee<int> employee;
        private List<ShowDTO<int>> showList;
        private List<ShowDTO<int>> showFilteredList;

        private void InitialiseList()
        {
            showList = service.GetAllShowsDTO().ToList();
            allShowsDataGridView.DataSource = showList;
            allShowsDataGridView.Columns["Id"].Visible = false;
            filteredShowsDataGridView.DataSource = showFilteredList;
        }

        private void MainStage_Load(object sender, EventArgs e)
        {
            dateTimePicker1.Format = DateTimePickerFormat.Custom;
            dateTimePicker1.CustomFormat = "dd.MM.yyyy";
            this.Text = "Logged as " + employee;
        }

        private void dateTimePicker1_ValueChanged(object sender, EventArgs e)
        {
            string date = dateTimePicker1.Text;
            var unfiltered = service.GetAllShowsDTO().ToList();
            showFilteredList = unfiltered.Where(x => x.Date == date).ToList();
            filteredShowsDataGridView.DataSource = showFilteredList;
            filteredShowsDataGridView.Columns["Id"].Visible = false;
        }

        private void allShowsDataGridView_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            if (allShowsDataGridView.CurrentCell != null)
            {
                int rowIndex = allShowsDataGridView.CurrentCell.RowIndex;
                ShowDTO<int> showDTO = allShowsDataGridView.Rows[rowIndex].DataBoundItem as ShowDTO<int>;
                if (showDTO != null)
                {
                    Buy buy = new Buy(service, showDTO);
                    buy.ShowDialog();
                }
            }
        }

        private void refreshButton_Click(object sender, EventArgs e)
        {
            InitialiseList();
        }

        private void logoutButton_Click(object sender, EventArgs e)
        {
            this.Hide();
            Login login = new Login(service);
            login.ShowDialog();
            this.Close();
        }
    }
}
