namespace BibliotecaSGBDLab2
{
    partial class Form1
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
            this.labelChild = new System.Windows.Forms.Label();
            this.labelParent = new System.Windows.Forms.Label();
            this.dataGridViewChild = new System.Windows.Forms.DataGridView();
            this.dataGridViewParent = new System.Windows.Forms.DataGridView();
            this.buttonSterge = new System.Windows.Forms.Button();
            this.buttonActualizeaza = new System.Windows.Forms.Button();
            this.buttonAdauga = new System.Windows.Forms.Button();
            this.flowLayoutPanel1 = new System.Windows.Forms.FlowLayoutPanel();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewChild)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewParent)).BeginInit();
            this.SuspendLayout();
            // 
            // labelChild
            // 
            this.labelChild.AutoSize = true;
            this.labelChild.Location = new System.Drawing.Point(735, 19);
            this.labelChild.Name = "labelChild";
            this.labelChild.Size = new System.Drawing.Size(0, 13);
            this.labelChild.TabIndex = 7;
            // 
            // labelParent
            // 
            this.labelParent.AutoSize = true;
            this.labelParent.Location = new System.Drawing.Point(21, 19);
            this.labelParent.Name = "labelParent";
            this.labelParent.Size = new System.Drawing.Size(0, 13);
            this.labelParent.TabIndex = 6;
            // 
            // dataGridViewChild
            // 
            this.dataGridViewChild.BackgroundColor = System.Drawing.SystemColors.GradientInactiveCaption;
            this.dataGridViewChild.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridViewChild.Location = new System.Drawing.Point(726, 44);
            this.dataGridViewChild.Name = "dataGridViewChild";
            this.dataGridViewChild.Size = new System.Drawing.Size(600, 182);
            this.dataGridViewChild.TabIndex = 5;
            // 
            // dataGridViewParent
            // 
            this.dataGridViewParent.BackgroundColor = System.Drawing.SystemColors.GradientInactiveCaption;
            this.dataGridViewParent.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridViewParent.Location = new System.Drawing.Point(12, 44);
            this.dataGridViewParent.Name = "dataGridViewParent";
            this.dataGridViewParent.Size = new System.Drawing.Size(669, 182);
            this.dataGridViewParent.TabIndex = 4;
            // 
            // buttonSterge
            // 
            this.buttonSterge.BackColor = System.Drawing.SystemColors.GradientInactiveCaption;
            this.buttonSterge.FlatAppearance.BorderColor = System.Drawing.SystemColors.GradientInactiveCaption;
            this.buttonSterge.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.buttonSterge.Font = new System.Drawing.Font("Palatino Linotype", 11.25F, System.Drawing.FontStyle.Bold);
            this.buttonSterge.Location = new System.Drawing.Point(778, 489);
            this.buttonSterge.Name = "buttonSterge";
            this.buttonSterge.Size = new System.Drawing.Size(76, 65);
            this.buttonSterge.TabIndex = 15;
            this.buttonSterge.Text = "Sterge Copil";
            this.buttonSterge.UseVisualStyleBackColor = false;
            this.buttonSterge.Click += new System.EventHandler(this.buttonSterge_Click);
            // 
            // buttonActualizeaza
            // 
            this.buttonActualizeaza.BackColor = System.Drawing.SystemColors.GradientInactiveCaption;
            this.buttonActualizeaza.FlatAppearance.BorderColor = System.Drawing.SystemColors.GradientInactiveCaption;
            this.buttonActualizeaza.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.buttonActualizeaza.Font = new System.Drawing.Font("Palatino Linotype", 11.25F, System.Drawing.FontStyle.Bold);
            this.buttonActualizeaza.Location = new System.Drawing.Point(645, 489);
            this.buttonActualizeaza.Name = "buttonActualizeaza";
            this.buttonActualizeaza.Size = new System.Drawing.Size(127, 65);
            this.buttonActualizeaza.TabIndex = 14;
            this.buttonActualizeaza.Text = "Actualizeaza Copil";
            this.buttonActualizeaza.UseVisualStyleBackColor = false;
            this.buttonActualizeaza.Click += new System.EventHandler(this.buttonActualizeaza_Click);
            // 
            // buttonAdauga
            // 
            this.buttonAdauga.BackColor = System.Drawing.SystemColors.GradientInactiveCaption;
            this.buttonAdauga.FlatAppearance.BorderColor = System.Drawing.SystemColors.GradientInactiveCaption;
            this.buttonAdauga.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.buttonAdauga.Font = new System.Drawing.Font("Palatino Linotype", 11.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.buttonAdauga.Location = new System.Drawing.Point(554, 489);
            this.buttonAdauga.Name = "buttonAdauga";
            this.buttonAdauga.Size = new System.Drawing.Size(85, 65);
            this.buttonAdauga.TabIndex = 13;
            this.buttonAdauga.Text = "Adauga Copil";
            this.buttonAdauga.UseVisualStyleBackColor = false;
            this.buttonAdauga.Click += new System.EventHandler(this.buttonAdauga_Click);
            // 
            // flowLayoutPanel1
            // 
            this.flowLayoutPanel1.Location = new System.Drawing.Point(454, 257);
            this.flowLayoutPanel1.Name = "flowLayoutPanel1";
            this.flowLayoutPanel1.Size = new System.Drawing.Size(487, 226);
            this.flowLayoutPanel1.TabIndex = 16;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.GradientActiveCaption;
            this.ClientSize = new System.Drawing.Size(1322, 566);
            this.Controls.Add(this.flowLayoutPanel1);
            this.Controls.Add(this.buttonSterge);
            this.Controls.Add(this.buttonActualizeaza);
            this.Controls.Add(this.buttonAdauga);
            this.Controls.Add(this.labelChild);
            this.Controls.Add(this.labelParent);
            this.Controls.Add(this.dataGridViewChild);
            this.Controls.Add(this.dataGridViewParent);
            this.Name = "Form1";
            this.Text = "Form1";
            this.Load += new System.EventHandler(this.Form1_Load);
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewChild)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewParent)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label labelChild;
        private System.Windows.Forms.Label labelParent;
        private System.Windows.Forms.DataGridView dataGridViewChild;
        private System.Windows.Forms.DataGridView dataGridViewParent;
        private System.Windows.Forms.Button buttonSterge;
        private System.Windows.Forms.Button buttonActualizeaza;
        private System.Windows.Forms.Button buttonAdauga;
        private System.Windows.Forms.FlowLayoutPanel flowLayoutPanel1;
    }
}

