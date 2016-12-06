using currency_converter.model;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using Xamarin.Forms;

namespace currency_converter.API
{
    public class HttpRequest
    {
        public static void RefreshRates(List<CurrencyModel> currencies, DataAccess db)
        {
            for (int i = 0; i < currencies.Count; i++)
            {
                var uri = string.Format("http://download.finance.yahoo.com/d/quotes?f=sl1d1t1&s=EUR{0}=X", currencies[i].code);
                var cb = new AsyncCallback(CallHandler);
                CallWebAsync(uri, currencies[i], db, cb);
            }
        }

        public static void CallWebAsync(string uri, CurrencyModel response, DataAccess db, AsyncCallback cb)
        {
            var request = HttpWebRequest.Create(uri);
            request.Method = "GET";
            var state = new Tuple<CurrencyModel, DataAccess, WebRequest>(response, db, request);

            request.BeginGetResponse(cb, state);
        }

        private static void CallHandler(IAsyncResult ar)
        {
            var state = (Tuple<CurrencyModel, DataAccess, WebRequest>)ar.AsyncState;
            var request = state.Item3;

            using (HttpWebResponse response = request.EndGetResponse(ar) as HttpWebResponse)
            {
                if (response.StatusCode == HttpStatusCode.OK)
                    using (StreamReader reader = new StreamReader(response.GetResponseStream()))
                    {
                        var db = state.Item2;
                        var content = reader.ReadToEnd();

                        var tokens = content.Split(',');

                        //Device.BeginInvokeOnMainThread(() => state.Item1.Text = "1 EUR = " + tokens[1] + " " + currenciesPicker.Items[currenciesPicker.SelectedIndex]);

                        Debug.WriteLine("ENTREI: no callhandler -> Code: " + state.Item1.code + " Name: " + state.Item1.name + " Value: " + tokens[1]);

                        db.UpdateCurrency(new CurrencyModel(state.Item1.code, state.Item1.name, double.Parse(tokens[1])));

                        Debug.WriteLine("Passei o update");
                    }
            }
        }

    }
}
