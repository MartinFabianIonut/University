using System;
using System.Windows.Forms;

namespace SeventhWeekCSharpServerClient.client
{
    public partial class Login : Form
    {
        private ClientController clientController;

        public Login(ClientController clientController)
        {
            InitializeComponent();
            this.clientController = clientController;
        }

        private void Login_Load(object sender, EventArgs e)
        {
        }

        private void loginButton_Click(object sender, EventArgs e)
        {
            if (usernameTextBox.Text.Length <= 0 || passwordTextBox.Text.Length <= 0) return;
            var username = usernameTextBox.Text.ToString();
            var password = passwordTextBox.Text.ToString();
            try {
                clientController.Login(username, password);
                this.Hide();
                var mainStage = new MainStage(clientController);
                mainStage.ShowDialog();
                this.Close();
            }
            catch (Exception ex) 
            {
                MessageBox.Show(@"Authentication failed! and " + ex.Message);
            }
        }
    }
}
