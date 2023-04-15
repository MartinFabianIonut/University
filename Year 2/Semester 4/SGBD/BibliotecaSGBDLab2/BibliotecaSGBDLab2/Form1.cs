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

namespace BibliotecaSGBDLab2
{
    public partial class Form1 : Form
    {

        private readonly string connectionString = ConfigurationManager.AppSettings["connectionString"];
        private readonly string updateCommand = ConfigurationManager.AppSettings["updateCommand"];
        private readonly string insertCommand = ConfigurationManager.AppSettings["insertCommand"];
        private readonly string deleteCommand = ConfigurationManager.AppSettings["deleteCommand"];
        private readonly List<string> paramsForInsert = new List<string> (ConfigurationManager.AppSettings["paramsForInsert"].Split(','));
        private readonly List<string> paramsForDelete = new List<string>(ConfigurationManager.AppSettings["paramsForDelete"].Split(','));
        private readonly List<string> paramsForUpdate = new List<string>(ConfigurationManager.AppSettings["paramsForUpdate"].Split(','));
        private readonly string selectFromParent = ConfigurationManager.AppSettings["selectFromParent"];
        private readonly string parent = ConfigurationManager.AppSettings["parent"];
        private readonly string selectFromChild = ConfigurationManager.AppSettings["selectFromChild"];
        private readonly string child = ConfigurationManager.AppSettings["child"];
        private readonly int size = Int32.Parse(ConfigurationManager.AppSettings["size"]);

        DataSet dataSet = new DataSet();
        SqlDataAdapter parentAdapter = new SqlDataAdapter();
        SqlDataAdapter childAdapter = new SqlDataAdapter();
        BindingSource parentBS = new BindingSource();
        BindingSource childBS = new BindingSource();

        private TextBox[] textBoxes;
        private Label[] labels;

        public Form1()
        {
            InitializeComponent();
            labelParent.Text = "Parent: " + parent;
            labelParent.Font = new Font("Palatino Linotype", 13.25f, FontStyle.Bold);
            labelChild.Text = "Child: " + child;
            labelChild.Font = new Font("Palatino Linotype", 13.25f, FontStyle.Bold);
            AddTextBoxesForCommands();
        }

        private void AddTextBoxesForCommands()
        {
            flowLayoutPanel1.WrapContents = true;
            flowLayoutPanel1.FlowDirection = FlowDirection.LeftToRight;
            textBoxes = new TextBox[size];
            labels = new Label[size];

            for (int i = 0; i < size; i++)
            {
                textBoxes[i] = new TextBox();
                labels[i] = new Label();
                labels[i].Font = new Font("Palatino Linotype", 11.25f, FontStyle.Bold);
                labels[i].TextAlign = ContentAlignment.MiddleRight;
                textBoxes[i].BackColor = SystemColors.GradientInactiveCaption;
                textBoxes[i].Font = new Font("Palatino Linotype", 10.25f, FontStyle.Regular);
                var columnName = char.ToUpper(paramsForInsert[i + 1][1]) + paramsForInsert[i + 1].Substring(2) + ":";
                labels[i].Text = columnName;
            }

            for (int i = 0; i < size; i++)
            {
                flowLayoutPanel1.Controls.Add(labels[i]);
                flowLayoutPanel1.Controls.Add(textBoxes[i]);
                var spaceLabel = new Label();
                spaceLabel.Size = new Size(10, 10);
                flowLayoutPanel1.Controls.Add(spaceLabel);
            }
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            try
            {
                using (var sqlConnection = new SqlConnection(connectionString))
                {
                    sqlConnection.Open();
                    parentAdapter.SelectCommand = new SqlCommand(selectFromParent, sqlConnection);
                    childAdapter.SelectCommand = new SqlCommand(selectFromChild, sqlConnection);
                    parentAdapter.Fill(dataSet, parent);
                    childAdapter.Fill(dataSet, child);

                    parentBS.DataSource = dataSet.Tables[parent];
                    dataGridViewParent.DataSource = parentBS;

                    // establishing the relationship between the tables
                    var parentColumn = dataSet.Tables[parent].Columns[paramsForInsert[0].Substring(1)];
                    var childColumn = dataSet.Tables[child].Columns[paramsForInsert[0].Substring(1)];

                    string relationName = "FK_" + parent + "_" + child;
                    var relation = new DataRelation(relationName, parentColumn, childColumn);
                    dataSet.Relations.Add(relation);
                    childBS.DataSource = parentBS;
                    childBS.DataMember = relationName;
                    dataGridViewChild.DataSource = childBS;

                    List<DataGridView> dataGridViews = new List<DataGridView>();
                    dataGridViews.Add(dataGridViewChild);
                    dataGridViews.Add(dataGridViewParent);

                    foreach(DataGridView dataGridView in dataGridViews)
                    {
                        dataGridView.DefaultCellStyle.Font = new Font("Palatino Linotype", 10);
                        dataGridView.DefaultCellStyle.BackColor = SystemColors.GradientInactiveCaption;
                        dataGridView.ColumnHeadersDefaultCellStyle.Font = new Font("Palatino Linotype", 11, FontStyle.Bold);
                        dataGridView.ColumnHeadersDefaultCellStyle.BackColor = Color.Lavender;
                        dataGridView.RowHeadersDefaultCellStyle.BackColor = Color.Lavender;
                        dataGridView.EnableHeadersVisualStyles = false;
                    }
                }
            }
            catch (Exception exception)
            {
                MessageBox.Show(exception.Message);
            }
        }

        private void RefreshChildTable()
        {
            using (var sqlConnection = new SqlConnection(connectionString))
            {
                sqlConnection.Open();
                childAdapter.SelectCommand = new SqlCommand(selectFromChild, sqlConnection);
                dataSet.Tables[child].Clear();
                childAdapter.Fill(dataSet, child);
            }
        }

        private void buttonAdauga_Click(object sender, EventArgs e)
        {
            bool allCompleted = true;
            foreach (var textBox in textBoxes)
            {
                if (textBox.Text.Length == 0)
                    allCompleted = false;
            }
            if (allCompleted)
                try
                {
                    var row = dataGridViewParent.CurrentCell.RowIndex;
                    var id_parent = (int)dataGridViewParent.Rows[row].Cells[0].Value;
                    using (var sqlConnection = new SqlConnection(connectionString))
                    {
                        sqlConnection.Open();
                        var command = new SqlCommand(insertCommand, sqlConnection);
                        command.Parameters.AddWithValue(paramsForInsert[0], id_parent);
                        for (int i = 0; i < size; i++)
                            command.Parameters.AddWithValue(paramsForInsert[i+1], textBoxes[i].Text);
                        command.ExecuteNonQuery();
                        MessageBox.Show("Success in inserting data into the child table: " + child);
                        RefreshChildTable();
                    }
                }
                catch (Exception exception)
                {
                    MessageBox.Show(exception.Message);
                }
            else
                MessageBox.Show("Not all text boxes has been filled in!");
        }

        private void buttonActualizeaza_Click(object sender, EventArgs e)
        {
            bool allCompleted = true;
            foreach (var textBox in textBoxes)
            {
                if (textBox.Text.Length == 0)
                    allCompleted = false;
            }
            if (allCompleted)
                try
                {
                    var row = dataGridViewChild.CurrentCell.RowIndex;
                    var id_child = (int)dataGridViewChild.Rows[row].Cells[0].Value;
                    using (var sqlConnection = new SqlConnection(connectionString))
                    {
                        sqlConnection.Open();
                        var command = new SqlCommand(updateCommand, sqlConnection);
                        command.Parameters.AddWithValue(paramsForUpdate[0], id_child);
                        for (int i = 1; i < size + 1; i++)
                            command.Parameters.AddWithValue(paramsForUpdate[i], textBoxes[i-1].Text);
                        command.ExecuteNonQuery();
                        MessageBox.Show("Success in updating the data in the child table: " + child);
                        RefreshChildTable();
                    }
                }
                catch (Exception exception)
                {
                    MessageBox.Show(exception.Message);
                }
            else
                MessageBox.Show("Not all text boxes has been filled in!");
        }

        private void buttonSterge_Click(object sender, EventArgs e)
        {
            if (dataGridViewChild.CurrentRow != null)
            {
                var row = dataGridViewChild.CurrentRow.Index;
                var id_child = (int)dataGridViewChild.Rows[row].Cells[0].Value;
                try
                {
                    using (var sqlConnection = new SqlConnection(connectionString))
                    {
                        sqlConnection.Open();
                        var command = new SqlCommand(deleteCommand, sqlConnection);
                        command.Parameters.AddWithValue(paramsForDelete[0], id_child);
                        command.ExecuteNonQuery();
                        RefreshChildTable();
                        MessageBox.Show("Success in deleting the record from the child table: " + child);
                    }
                }
                catch (Exception exception)
                {
                    MessageBox.Show(exception.Message);
                }
            }
            else
                MessageBox.Show("Nothing selected!");
        }
    }
}
