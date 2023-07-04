namespace BibliotecaSGBD
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
            this.dataGridViewParinte = new System.Windows.Forms.DataGridView();
            this.dataGridViewCopil = new System.Windows.Forms.DataGridView();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.buttonAdauga = new System.Windows.Forms.Button();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.textBoxSerie = new System.Windows.Forms.TextBox();
            this.textBoxNumar = new System.Windows.Forms.TextBox();
            this.textBoxEmitere = new System.Windows.Forms.TextBox();
            this.buttonActualizeaza = new System.Windows.Forms.Button();
            this.buttonSterge = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewParinte)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewCopil)).BeginInit();
            this.SuspendLayout();
            // 
            // dataGridViewParinte
            // 
            this.dataGridViewParinte.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridViewParinte.Location = new System.Drawing.Point(50, 52);
            this.dataGridViewParinte.Name = "dataGridViewParinte";
            this.dataGridViewParinte.Size = new System.Drawing.Size(669, 182);
            this.dataGridViewParinte.TabIndex = 0;
            // 
            // dataGridViewCopil
            // 
            this.dataGridViewCopil.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridViewCopil.Location = new System.Drawing.Point(764, 52);
            this.dataGridViewCopil.Name = "dataGridViewCopil";
            this.dataGridViewCopil.Size = new System.Drawing.Size(600, 182);
            this.dataGridViewCopil.TabIndex = 1;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(50, 33);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(86, 13);
            this.label1.TabIndex = 2;
            this.label1.Text = "Parinte: Utilizator";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(764, 33);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(127, 13);
            this.label2.TabIndex = 3;
            this.label2.Text = "Copil: Cos de cumparaturi";
            // 
            // buttonAdauga
            // 
            this.buttonAdauga.Location = new System.Drawing.Point(50, 382);
            this.buttonAdauga.Name = "buttonAdauga";
            this.buttonAdauga.Size = new System.Drawing.Size(75, 38);
            this.buttonAdauga.TabIndex = 4;
            this.buttonAdauga.Text = "Adauga Copil";
            this.buttonAdauga.UseVisualStyleBackColor = true;
            this.buttonAdauga.Click += new System.EventHandler(this.buttonAdauga_Click);
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(50, 271);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(34, 13);
            this.label3.TabIndex = 5;
            this.label3.Text = "Serie:";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(50, 300);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(41, 13);
            this.label4.TabIndex = 6;
            this.label4.Text = "Numar:";
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(50, 327);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(45, 13);
            this.label5.TabIndex = 7;
            this.label5.Text = "Emitere:";
            // 
            // textBoxSerie
            // 
            this.textBoxSerie.Location = new System.Drawing.Point(90, 264);
            this.textBoxSerie.Name = "textBoxSerie";
            this.textBoxSerie.Size = new System.Drawing.Size(100, 20);
            this.textBoxSerie.TabIndex = 8;
            // 
            // textBoxNumar
            // 
            this.textBoxNumar.Location = new System.Drawing.Point(97, 293);
            this.textBoxNumar.Name = "textBoxNumar";
            this.textBoxNumar.Size = new System.Drawing.Size(100, 20);
            this.textBoxNumar.TabIndex = 9;
            // 
            // textBoxEmitere
            // 
            this.textBoxEmitere.Location = new System.Drawing.Point(101, 320);
            this.textBoxEmitere.Name = "textBoxEmitere";
            this.textBoxEmitere.Size = new System.Drawing.Size(100, 20);
            this.textBoxEmitere.TabIndex = 10;
            // 
            // buttonActualizeaza
            // 
            this.buttonActualizeaza.Location = new System.Drawing.Point(161, 382);
            this.buttonActualizeaza.Name = "buttonActualizeaza";
            this.buttonActualizeaza.Size = new System.Drawing.Size(75, 38);
            this.buttonActualizeaza.TabIndex = 11;
            this.buttonActualizeaza.Text = "Actualizeaza Copil";
            this.buttonActualizeaza.UseVisualStyleBackColor = true;
            this.buttonActualizeaza.Click += new System.EventHandler(this.buttonActualizeaza_Click);
            // 
            // buttonSterge
            // 
            this.buttonSterge.Location = new System.Drawing.Point(264, 382);
            this.buttonSterge.Name = "buttonSterge";
            this.buttonSterge.Size = new System.Drawing.Size(75, 38);
            this.buttonSterge.TabIndex = 12;
            this.buttonSterge.Text = "Sterge Copil";
            this.buttonSterge.UseVisualStyleBackColor = true;
            this.buttonSterge.Click += new System.EventHandler(this.buttonSterge_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1381, 450);
            this.Controls.Add(this.buttonSterge);
            this.Controls.Add(this.buttonActualizeaza);
            this.Controls.Add(this.textBoxEmitere);
            this.Controls.Add(this.textBoxNumar);
            this.Controls.Add(this.textBoxSerie);
            this.Controls.Add(this.label5);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.buttonAdauga);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.dataGridViewCopil);
            this.Controls.Add(this.dataGridViewParinte);
            this.Name = "Form1";
            this.Text = "Form1";
            this.Load += new System.EventHandler(this.Form1_Load);
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewParinte)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewCopil)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.DataGridView dataGridViewParinte;
        private System.Windows.Forms.DataGridView dataGridViewCopil;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Button buttonAdauga;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.TextBox textBoxSerie;
        private System.Windows.Forms.TextBox textBoxNumar;
        private System.Windows.Forms.TextBox textBoxEmitere;
        private System.Windows.Forms.Button buttonActualizeaza;
        private System.Windows.Forms.Button buttonSterge;
    }
}

