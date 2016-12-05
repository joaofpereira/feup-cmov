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
            dbConn.DropTable<WalletModel>();
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

            if (!TableExists("WalletModel"))
            {
                dbConn.CreateTable<WalletModel>();
                
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

        public List<CurrencyModel> GetAllCurrencyNames()
        {
            return dbConn.Query<CurrencyModel>("Select code From [CurrencyModel]");
        }

        public int deleteCurrency(CurrencyModel aCurrency)
        {
            return dbConn.Delete(aCurrency);
        }

        public void UpdateCurrencyToEUR(CurrencyModel aCurrency)
        {/*
            List<CurrencyModel> oldCurrency = dbConn.Query<CurrencyModel>("Select * From [CurrencyModel] where  [CurrencyModel].code = '" + aCurrency.code + "'");
            deleteCurrency(oldCurrency[0]);
            SaveCurrency(aCurrency);*/
            dbConn.Update(aCurrency);
         }


        public int DeleteWalletEntry(WalletModel aWalletEntry)
        {
            return dbConn.Delete(aWalletEntry);
        }

        public List<WalletModel> GetWalletEntries()
        {
            return dbConn.Query<WalletModel>("Select * From [WalletModel]");
        }

        public int SaveWalletEntry(WalletModel aWalletEntry)
        {
            return dbConn.Insert(aWalletEntry);
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