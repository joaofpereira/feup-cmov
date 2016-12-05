using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Xamarin.Forms;

namespace currency_converter
{
    public partial class App : Application
    {
        public static DataAccess dbUtils;
        public App()
        {
            MainPage = new NavigationPage(new MainPage());
        }
        public static DataAccess DAUtil
        {
            get
            {
                if (dbUtils == null)
                {
                    dbUtils = new DataAccess();
                }
                return dbUtils;
            }
        }
        protected override void OnStart()
        {
            //DAUtil.DropTables();
            //DAUtil.TableExists("CurrencyModel");
            DAUtil.CreateTablesIfNotExists();
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
