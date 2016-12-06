using currency_converter.API;
using currency_converter.model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Xamarin.Forms;

namespace currency_converter
{
    public partial class App : Application
    {
        public DataAccess db;

        public App()
        {
            db = DataAccess.GetDB;
            MainPage = new NavigationPage(new MainPage());
        }
        
        protected override void OnStart()
        {
            db.DropTables();
            db.CreateTablesIfNotExists();

            List<CurrencyModel> currencies = db.GetAllCurrency();
            HttpRequest.RefreshRates(currencies, db);

            db.printListOfCurrencies();
        }

        protected override void OnSleep()
        {
            // Handle when your app sleeps
        }

        protected override void OnResume()
        {
            // Handle when your app resumes
        }
    }
}
