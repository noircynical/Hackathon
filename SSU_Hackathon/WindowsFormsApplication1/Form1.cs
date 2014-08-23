using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApplication1
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            label3.Visible = false;
            label4.Visible = false;
        }

        private void button1_Click(object sender, EventArgs e)
        {

            Form2 frmForm2 = new Form2();
            frmForm2.Show();
        }

        private void pictureBox1_Click(object sender, EventArgs e)
        {

        }

        private void radioButton1_CheckedChanged(object sender, EventArgs e)
        {
            pictureBox1.Image = System.Drawing.Image.FromFile(@"C:\Users\y\Desktop\영화\1.jpg");
            int b = 699000;
            label3.Visible = true;
            label3.Text = Convert.ToString(b);
        }

        private void radioButton2_CheckedChanged(object sender, EventArgs e)
        {
            pictureBox1.Image = System.Drawing.Image.FromFile(@"C:\Users\y\Desktop\영화\2.jpg");
            int a = 713570;
            label4.Visible = true;
            label3.Text = Convert.ToString(a);

        }
    }
}
