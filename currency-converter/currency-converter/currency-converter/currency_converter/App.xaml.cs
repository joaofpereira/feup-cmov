﻿using currency_converter.API;
using currency_converter.model;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using Xamarin.Forms;

namespace currency_converter
{
    public partial class App : Application
    {
        public DataAccess db;
        public HttpRequest httpRequest;

        public App()
        {
            db = DataAccess.GetDB;
            httpRequest = new HttpRequest();

            //db.DropTables();
            db.CreateTablesIfNotExists();

            List<CurrencyModel> currencies = db.GetAllCurrency();
            httpRequest.RefreshRates(currencies, db);

            db.printListOfCurrencies();

            MainPage = new NavigationPage(new MainPage());
        }
        
        protected override void OnStart()
        {
            
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
