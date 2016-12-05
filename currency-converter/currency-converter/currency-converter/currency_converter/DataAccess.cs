using SQLite;
using Xamarin.Forms;
using currency_converter.model;
using System.Collections.Generic;

namespace currency_converter
{
    public class DataAccess
    {
        SQLiteConnection dbConn;
        public DataAccess()
        {
            dbConn = DependencyService.Get<ISQLite>().GetConnection();
            // create the table(s)
            dbConn.CreateTable<Currency>();
        }
        public List<Currency> GetAllCurrency()
        {
            return dbConn.Query<Currency>("Select * From [Currency]");
        }
        public int SaveCurrency(Currency aCurrency)
        {
            return dbConn.Insert(aCurrency);
        }
    }
}