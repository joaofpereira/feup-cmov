using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Reflection;
using Xamarin.Forms;

namespace currency_converter
{
    public class App : Application
    {
        List<Currency> currencylist = new List<Currency>();

        public App()
        {
           

            // The root page of your application
            var content = new ContentPage
            {
                Title = "Currency Converter",
                Content = new StackLayout
                {
                    VerticalOptions = LayoutOptions.Center,
                    Children = {
                        new Label {
                            HorizontalTextAlignment = TextAlignment.Center,
                            Text = "Welcome to Xamarin Forms!"
                        }
                    }
                }
            };
            readCSVToList();
            MainPage = new NavigationPage(content);
        }

        protected override void OnStart()
        {
            // Handle when your app starts
        }

        protected override void OnSleep()
        {
            // Handle when your app sleeps
        }

        protected override void OnResume()
        {
            // Handle when your app resumes
        }

        private  void readCSVToList()
        {
            var assembly = typeof(ContentPage).GetTypeInfo().Assembly;
            Stream stream = assembly.GetManifestResourceStream("currency_converter.resources.currency.csv");
            System.IO.StreamReader file = new System.IO.StreamReader(stream);

            while (!file.EndOfStream)
            {
                var currencyLine = file.ReadLine();
                var tokens = currencyLine.Split(',');

                Debug.WriteLine(currencyLine);

                Currency c = new Currency(tokens[0], tokens[1]);

                currencylist.Add(c);
            }


        }
    }
}
