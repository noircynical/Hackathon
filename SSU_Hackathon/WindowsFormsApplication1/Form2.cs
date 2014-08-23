using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

using System.IO;
using System.Net;
using System.Xml;

namespace WindowsFormsApplication1
{

    public partial class Form2 : Form
    {
        public Form2()
        {
            InitializeComponent();
        }

        private void Form2_Load(object sender, EventArgs e)
        {
            label2.Visible = false;
            label3.Visible = false;
            label4.Visible = false;
            label5.Visible = false;

        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {

        }

        private void button2_Click(object sender, System.EventArgs e)
        {
            //XmlDocument xml = new XmlDocument();
            XmlReader reader = XmlReader.Create("http://openapi.naver.com/search?key=244f0fbe47576aa19d05fc5a79ebd70f&query=" + textBox1.Text + "&display=5&start=1&target=shop&sort=sim");


            
                if (reader != null)
                {
                    reader.ReadToFollowing("lprice");
                    string value = reader.ReadElementContentAsString();
                    label2.Text = value;
                }
          

            /*
            XmlReader reader_3 = XmlReader.Create("http://openapi.naver.com/search?key=244f0fbe47576aa19d05fc5a79ebd70f&query=" + textBox1.Text + "&display=5&start=1&target=shop&sort=sim");
            reader_3.ReadToFollowing("lprice");
            string value_3 = reader_3.ReadElementContentAsString();
            label4.Text = value_3;

            XmlReader reader_4 = XmlReader.Create("http://openapi.naver.com/search?key=244f0fbe47576aa19d05fc5a79ebd70f&query=" + textBox1.Text + "&display=5&start=1&target=shop&sort=sim");
            reader_4.ReadToFollowing("lprice");
            string value_4 = reader_4.ReadElementContentAsString();
            label5.Text = value_4;
            */
            Form3 frmForm3 = new Form3(label2.Text, label3.Text, label4.Text, label5.Text);
            frmForm3.ShowDialog();
        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {

        }

    }
}
