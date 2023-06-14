namespace ASaseaSaptamana
{
    partial class MainStage
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.allShowsDataGridView = new System.Windows.Forms.DataGridView();
            this.filteredShowsDataGridView = new System.Windows.Forms.DataGridView();
            this.dateTimePicker1 = new System.Windows.Forms.DateTimePicker();
            this.refreshButton = new System.Windows.Forms.Button();
            this.logoutButton = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.allShowsDataGridView)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.filteredShowsDataGridView)).BeginInit();
            this.SuspendLayout();
            // 
            // allShowsDataGridView
            // 
            this.allShowsDataGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.allShowsDataGridView.Location = new System.Drawing.Point(74, 35);
            this.allShowsDataGridView.Name = "allShowsDataGridView";
            this.allShowsDataGridView.Size = new System.Drawing.Size(659, 150);
            this.allShowsDataGridView.TabIndex = 0;
            this.allShowsDataGridView.CellContentClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.allShowsDataGridView_CellContentClick);
            // 
            // filteredShowsDataGridView
            // 
            this.filteredShowsDataGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.filteredShowsDataGridView.Location = new System.Drawing.Point(74, 265);
            this.filteredShowsDataGridView.Name = "filteredShowsDataGridView";
            this.filteredShowsDataGridView.Size = new System.Drawing.Size(659, 150);
            this.filteredShowsDataGridView.TabIndex = 1;
            // 
            // dateTimePicker1
            // 
            this.dateTimePicker1.Location = new System.Drawing.Point(74, 214);
            this.dateTimePicker1.Name = "dateTimePicker1";
            this.dateTimePicker1.Size = new System.Drawing.Size(200, 20);
            this.dateTimePicker1.TabIndex = 2;
            this.dateTimePicker1.ValueChanged += new System.EventHandler(this.dateTimePicker1_ValueChanged);
            // 
            // refreshButton
            // 
            this.refreshButton.Location = new System.Drawing.Point(658, 200);
            this.refreshButton.Name = "refreshButton";
            this.refreshButton.Size = new System.Drawing.Size(75, 23);
            this.refreshButton.TabIndex = 3;
            this.refreshButton.Text = "Refresh";
            this.refreshButton.UseVisualStyleBackColor = true;
            this.refreshButton.Click += new System.EventHandler(this.refreshButton_Click);
            // 
            // logoutButton
            // 
            this.logoutButton.Location = new System.Drawing.Point(658, 229);
            this.logoutButton.Name = "logoutButton";
            this.logoutButton.Size = new System.Drawing.Size(75, 23);
            this.logoutButton.TabIndex = 4;
            this.logoutButton.Text = "Logout";
            this.logoutButton.UseVisualStyleBackColor = true;
            this.logoutButton.Click += new System.EventHandler(this.logoutButton_Click);
            // 
            // MainStage
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(800, 450);
            this.Controls.Add(this.logoutButton);
            this.Controls.Add(this.refreshButton);
            this.Controls.Add(this.dateTimePicker1);
            this.Controls.Add(this.filteredShowsDataGridView);
            this.Controls.Add(this.allShowsDataGridView);
            this.Name = "MainStage";
            this.Text = "MainStage";
            this.Load += new System.EventHandler(this.MainStage_Load);
            ((System.ComponentModel.ISupportInitialize)(this.allShowsDataGridView)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.filteredShowsDataGridView)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.DataGridView allShowsDataGridView;
        private System.Windows.Forms.DataGridView filteredShowsDataGridView;
        private System.Windows.Forms.DateTimePicker dateTimePicker1;
        private System.Windows.Forms.Button refreshButton;
        private System.Windows.Forms.Button logoutButton;
    }
}