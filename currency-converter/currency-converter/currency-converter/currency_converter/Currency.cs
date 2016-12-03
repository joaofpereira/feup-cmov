using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace currency_converter
{
    class Currency
    {

        private string code { get; set; }
        private string name { get; set; }
        private float toCurrency { get; set; }


        public Currency(string code, string name)
        {
            this.code = code;
            this.name = name;
            this.toCurrency = 1;
        }

    }
}
