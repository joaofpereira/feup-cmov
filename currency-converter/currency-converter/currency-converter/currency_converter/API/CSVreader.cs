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

                    CurrencyModel vCurrency = null;

                    if (tokens[0] == "EUR")
                        vCurrency = new CurrencyModel(tokens[0], tokens[1], true);
                    else
                        vCurrency = new CurrencyModel(tokens[0], tokens[1]);

                    Debug.WriteLine("token code: " + vCurrency.code + " token name: " + vCurrency.name);
                    
                    DataAccess.GetDB.SaveCurrency(vCurrency);

                }
            }
        }
        public void printCurrencyTableContent()
        {
           List<CurrencyModel> dbContent = new List<CurrencyModel>();
           dbContent = DataAccess.GetDB.GetAllCurrency();

            for ( int i = 0; i < dbContent.Count; i++)
            {
                Debug.WriteLine("Currency code: " + dbContent[i].code + " Currency name: " + dbContent[i].name + "IsFavourite: " + dbContent[i].isFavourite);
            }

        }
    }
}
