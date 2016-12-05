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
            dbConn.CreateTable<CurrencyModel>();
        }
        public List<CurrencyModel> GetAllCurrency()
        {
            return dbConn.Query<CurrencyModel>("Select * From [Currency]");
        }
        public int SaveCurrency(CurrencyModel aCurrency)
        {
            return dbConn.Insert(aCurrency);
        }
    }
}