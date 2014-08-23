using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

using System.Net;
using System.IO;
using System.Threading;

namespace WindowsFormsApplication1
{
    public partial class Form3 : Form
    {
        public Form3(string strName,string strName2,string strName3, string strName4)
        {
            InitializeComponent();
            label1.Text = strName;
            label2.Text = strName2;
            label3.Text = strName3;
            label4.Text = strName4;
        }

        private void Form3_Load(object sender, EventArgs e)
        {
            button1.Visible = false;
        }

        private void button1_Click(object sender, EventArgs e)
        {

        }

        private void checkBox1_CheckedChanged(object sender, EventArgs e)
        {
            button1.Visible = true;
        }

        private void checkBox2_CheckedChanged(object sender, EventArgs e)
        {
            button1.Visible = true;
        }

        private void checkBox3_CheckedChanged(object sender, EventArgs e)
        {
            button1.Visible = true;
        }

        private void checkBox4_CheckedChanged(object sender, EventArgs e)
        {
            button1.Visible = true;
        }

        private void checkBox5_CheckedChanged(object sender, EventArgs e)
        {
            button1.Visible = true;
        }

    }
}
