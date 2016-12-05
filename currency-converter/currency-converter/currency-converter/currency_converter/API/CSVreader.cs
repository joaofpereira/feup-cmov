using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Diagnostics;
using System.IO;

using System.Reflection;
using Xamarin.Forms;
using currency_converter;
using currency_converter.model;

namespace currency_converter.API
{
    public class CSVreader
    {
         public CSVreader()
        {
        }

        public void readCSVToDB()
        {
            var assembly = typeof(CSVreader).GetTypeInfo().Assembly;
            Stream stream = assembly.GetManifestResourceStream("currency_converter.resources.currency.csv");
            using (var reader = new System.IO.StreamReader(stream))
            {

                while (!reader.EndOfStream)
                {
                    var currencyLine = reader.ReadLine();
                    var tokens = currencyLine.Split(',');

                    var vCurrency = new CurrencyModel()
                    {
                        code = tokens[0],
                        name = tokens[1],
                        toCurrency = 1
                    };
                    Debug.WriteLine("token code: " + vCurrency.code + " token name: " + vCurrency.name);
                    
                    App.dbUtils.SaveCurrency(vCurrency);

                }
            }
        }
        public void printCurrencyTableContent()
        {
            List<CurrencyModel> dbContent = new List<CurrencyModel>();
           dbContent = App.dbUtils.GetAllCurrency();

            for ( int i = 0; i < dbContent.Count; i++)
            {
                Debug.WriteLine("Currency code: " + dbContent[i].code + " Currency name: " + dbContent[i].name);
            }

        }
    }
}
