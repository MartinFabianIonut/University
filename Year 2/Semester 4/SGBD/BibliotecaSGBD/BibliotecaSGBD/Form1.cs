using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using Microsoft.Data.SqlClient;
using System.Configuration;

namespace BibliotecaSGBD
{
    public partial class Form1 : Form
    {
        private readonly string connectionString = ConfigurationManager.AppSettings["connectionString"];
        private readonly string updateCommand = ConfigurationManager.AppSettings["updateCommand"];
        private readonly string insertCommand = ConfigurationManager.AppSettings["insertCommand"];
        private readonly string deleteCommand = ConfigurationManager.AppSettings["deleteCommand"];
        private readonly string paramsForInsert = ConfigurationManager.AppSettings["paramsForInsert"];
        private readonly string paramsForDelete = ConfigurationManager.AppSettings["paramsForDelete"];
        private readonly string paramsForUpdate = ConfigurationManager.AppSettings["paramsForUpdate"];

        DataSet dataSet = new DataSet();
        SqlDataAdapter utilizatorAdapter = new SqlDataAdapter();
        SqlDataAdapter cosUtilizatorAdapter = new SqlDataAdapter();
        BindingSource utilizatorBS = new BindingSource();
        BindingSource cosBD = new BindingSource();

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            try
            {
                using (var sqlConnection = new SqlConnection(connectionString))
                {
                    sqlConnection.Open();
                    utilizatorAdapter.SelectCommand = new SqlCommand("SELECT * FROM Utilizatori", sqlConnection);
                    cosUtilizatorAdapter.SelectCommand = new SqlCommand("SELECT * FROM Cos_de_cumparaturi", sqlConnection);
                    utilizatorAdapter.Fill(dataSet, "Utilizatori");
                    cosUtilizatorAdapter.Fill(dataSet, "Cos_de_cumparaturi");

                    utilizatorBS.DataSource = dataSet.Tables["Utilizatori"];
                    dataGridViewParinte.DataSource = utilizatorBS;

                    var utilizatorColumn = dataSet.Tables["Utilizatori"].Columns["id_utilizator"];
                    var cosUtilizatorColumn = dataSet.Tables["Cos_de_cumparaturi"].Columns["id_utilizator"];

                    var relation = new DataRelation("FK_Utilizatori_Cos_de_cumparaturi", utilizatorColumn, cosUtilizatorColumn);
                    dataSet.Relations.Add(relation);
                    cosBD.DataSource = utilizatorBS;
                    cosBD.DataMember = "FK_Utilizatori_Cos_de_cumparaturi";
                    dataGridViewCopil.DataSource = cosBD;
                }
            }
            catch (Exception exception)
            {
                MessageBox.Show(exception.Message);
            }
        }

        private void buttonAdauga_Click(object sender, EventArgs e)
        {
            if (textBoxSerie.Text.Length > 0 && textBoxNumar.Text.Length > 0 && textBoxEmitere.Text.Length > 0)
                try
                {
                    var row = dataGridViewParinte.CurrentCell.RowIndex;
                    var lenght = dataGridViewCopil.ColumnCount;
                    var id_utilizator = (int)dataGridViewParinte.Rows[row].Cells[0].Value;
                    using (var sqlConnection = new SqlConnection(connectionString))
                    {
                        sqlConnection.Open();
                        var command = new SqlCommand(insertCommand,sqlConnection);
                        command.Parameters.AddWithValue("@id_utilizator", id_utilizator);
                        command.Parameters.AddWithValue("@serie", textBoxSerie.Text);
                        command.Parameters.AddWithValue("@numar", textBoxNumar.Text);
                        command.Parameters.AddWithValue("@emitere", textBoxEmitere.Text);
                        command.ExecuteNonQuery();
                        MessageBox.Show("Succes!");
                        RefreshChildTable();
                        textBoxSerie.Clear();
                        textBoxNumar.Clear();
                        textBoxEmitere.Clear();
                    }
                }
                catch (Exception exception)
                {
                    MessageBox.Show(exception.Message);
                }
            else
                MessageBox.Show("Nu toate celulele despre serie, numar, emitere au fost completate!");
        }

        private void buttonActualizeaza_Click(object sender, EventArgs e)
        {
            if(textBoxSerie.Text.Length > 0 && textBoxNumar.Text.Length > 0 && textBoxEmitere.Text.Length > 0)
                try
                {
                    var row = dataGridViewCopil.CurrentCell.RowIndex;
                    var lenght = dataGridViewCopil.ColumnCount;
                    var id_cos = (int)dataGridViewCopil.Rows[row].Cells[0].Value;
                    var id_utilizator = (int)dataGridViewCopil.Rows[row].Cells[1].Value;
                    using (var sqlConnection = new SqlConnection(connectionString))
                    {
                        sqlConnection.Open();
                        var command = new SqlCommand(updateCommand,sqlConnection);
                        command.Parameters.AddWithValue("@id_cos", id_cos);
                        command.Parameters.AddWithValue("@id_utilizator", id_utilizator);
                        command.Parameters.AddWithValue("@serie", textBoxSerie.Text);
                        command.Parameters.AddWithValue("@numar", textBoxNumar.Text);
                        command.Parameters.AddWithValue("@emitere", textBoxEmitere.Text);
                        command.ExecuteNonQuery();
                        MessageBox.Show("Succes!");
                        RefreshChildTable();
                        textBoxSerie.Clear();
                        textBoxNumar.Clear();
                        textBoxEmitere.Clear();
                    }
                }
                catch (Exception exception)
                {
                    MessageBox.Show(exception.Message);
                }
            else
                MessageBox.Show("Nu toate celulele despre serie, numar, emitere au fost completate!");
        }

        private void buttonSterge_Click(object sender, EventArgs e)
        {

            if (dataGridViewCopil.CurrentRow != null)
            {
                var row = dataGridViewCopil.CurrentRow.Index;
                var lenght = dataGridViewCopil.ColumnCount;
                var id_cos = (int)dataGridViewCopil.Rows[row].Cells[0].Value;
                try
                {
                    using (var sqlConnection = new SqlConnection(connectionString))
                    {
                        sqlConnection.Open();
                        var command = new SqlCommand(deleteCommand,sqlConnection);
                        command.Parameters.AddWithValue("@id_cos", id_cos);
                        command.ExecuteNonQuery();
                        RefreshChildTable();
                        MessageBox.Show("Succes!");
                    }
                }
                catch (Exception exception)
                {
                    MessageBox.Show(exception.Message);
                }
            }
            else
                MessageBox.Show("Nimic selectat");
        }

        private void RefreshChildTable()
        {
            using (var sqlConnection = new SqlConnection(connectionString))
            {
                sqlConnection.Open();
                cosUtilizatorAdapter.SelectCommand = new SqlCommand("SELECT * FROM Cos_de_cumparaturi", sqlConnection);
                dataSet.Tables["Cos_de_cumparaturi"].Clear();
                cosUtilizatorAdapter.Fill(dataSet, "Cos_de_cumparaturi");
            }
        }

        private void dataGridViewCopil_CellValueChanged(object sender, DataGridViewCellEventArgs e)
        {
            if (e.ColumnIndex == 0) {
                var c = e.ToString();
            }
        }

        private void dataGridViewCopil_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void dataGridViewCopil_Leave(object sender, EventArgs e)
        {
            if(e.Equals(null)) { }
        }

        private void dataGridViewCopil_RowLeave(object sender, DataGridViewCellEventArgs e)
        {
            if(e.ColumnIndex == 0) { }
        }
    }
}
