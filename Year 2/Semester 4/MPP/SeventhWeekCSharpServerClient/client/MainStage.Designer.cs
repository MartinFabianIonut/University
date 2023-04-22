namespace SeventhWeekCSharpServerClient.client
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
            this.logoutButton = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.allShowsDataGridView)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.filteredShowsDataGridView)).BeginInit();
            this.SuspendLayout();
            // 
            // allShowsDataGridView
            // 
            this.allShowsDataGridView.BackgroundColor = System.Drawing.SystemColors.GradientActiveCaption;
            this.allShowsDataGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.allShowsDataGridView.Location = new System.Drawing.Point(74, 35);
            this.allShowsDataGridView.Name = "allShowsDataGridView";
            this.allShowsDataGridView.Size = new System.Drawing.Size(848, 356);
            this.allShowsDataGridView.TabIndex = 0;
            this.allShowsDataGridView.CellContentClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.allShowsDataGridView_CellContentClick);
            // 
            // filteredShowsDataGridView
            // 
            this.filteredShowsDataGridView.BackgroundColor = System.Drawing.SystemColors.GradientActiveCaption;
            this.filteredShowsDataGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.filteredShowsDataGridView.Location = new System.Drawing.Point(74, 433);
            this.filteredShowsDataGridView.Name = "filteredShowsDataGridView";
            this.filteredShowsDataGridView.Size = new System.Drawing.Size(848, 200);
            this.filteredShowsDataGridView.TabIndex = 1;
            // 
            // dateTimePicker1
            // 
            this.dateTimePicker1.Font = new System.Drawing.Font("Modern No. 20", 12F);
            this.dateTimePicker1.Location = new System.Drawing.Point(74, 402);
            this.dateTimePicker1.Name = "dateTimePicker1";
            this.dateTimePicker1.Size = new System.Drawing.Size(239, 25);
            this.dateTimePicker1.TabIndex = 2;
            this.dateTimePicker1.ValueChanged += new System.EventHandler(this.dateTimePicker1_ValueChanged);
            // 
            // logoutButton
            // 
            this.logoutButton.BackColor = System.Drawing.SystemColors.GradientActiveCaption;
            this.logoutButton.FlatAppearance.BorderColor = System.Drawing.SystemColors.GradientActiveCaption;
            this.logoutButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.logoutButton.Font = new System.Drawing.Font("Modern No. 20", 12F, System.Drawing.FontStyle.Bold);
            this.logoutButton.Location = new System.Drawing.Point(1061, 600);
            this.logoutButton.Name = "logoutButton";
            this.logoutButton.Size = new System.Drawing.Size(84, 33);
            this.logoutButton.TabIndex = 4;
            this.logoutButton.Text = "Logout";
            this.logoutButton.UseVisualStyleBackColor = false;
            this.logoutButton.Click += new System.EventHandler(this.logoutButton_Click);
            // 
            // MainStage
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.GradientInactiveCaption;
            this.ClientSize = new System.Drawing.Size(1184, 661);
            this.Controls.Add(this.logoutButton);
            this.Controls.Add(this.dateTimePicker1);
            this.Controls.Add(this.filteredShowsDataGridView);
            this.Controls.Add(this.allShowsDataGridView);
            this.Name = "MainStage";
            this.Text = "MainStage";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.MainStage_FormClosing);
            this.Load += new System.EventHandler(this.MainStage_Load);
            ((System.ComponentModel.ISupportInitialize)(this.allShowsDataGridView)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.filteredShowsDataGridView)).EndInit();
            this.ResumeLayout(false);
        }

        #endregion

        private System.Windows.Forms.DataGridView allShowsDataGridView;
        private System.Windows.Forms.DataGridView filteredShowsDataGridView;
        private System.Windows.Forms.DateTimePicker dateTimePicker1;
        private System.Windows.Forms.Button logoutButton;
    }
}