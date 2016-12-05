using SQLite;
using Xamarin.Forms;
using System.Collections.Generic;
using System.Diagnostics;

using currency_converter.model;
using currency_converter.API;

namespace currency_converter
{
    public class DataAccess
    {
        SQLiteConnection dbConn;
        public DataAccess()
        {
            dbConn = DependencyService.Get<ISQLite>().GetConnection();
        }

        public void DropTables()
        {
            dbConn.DropTable<CurrencyModel>();
        }

        public void CreateTablesIfNotExists()
        {
            if(!TableExists("CurrencyModel"))
            {
                dbConn.CreateTable<CurrencyModel>();

                CSVreader reader = new CSVreader();
                reader.readCSVToDB();
                reader.printCurrencyTableContent();
            }
        }

        public List<CurrencyModel> GetAllCurrency()
        {
            return dbConn.Query<CurrencyModel>("Select * From [CurrencyModel]");
        }
        public int SaveCurrency(CurrencyModel aCurrency)
        {
            return dbConn.Insert(aCurrency);
        }

        public bool TableExists(string tableName)
        {
            var tableExistsQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "';";
            var result = dbConn.ExecuteScalar<string>(tableExistsQuery);

            if (result == null)
            {
                Debug.WriteLine("Result: Nulo");
                Debug.WriteLine("Esta nulo");
                return false;
            }
            else
            {
                Debug.WriteLine("Result: " + result.ToString());
                Debug.WriteLine("Nao esta nulo!");
                return true;
            }
        }
    }
}