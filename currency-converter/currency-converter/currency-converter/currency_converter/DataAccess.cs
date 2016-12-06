using SQLite;
using Xamarin.Forms;
using System.Collections.Generic;
using System.Diagnostics;

using currency_converter.model;
using currency_converter.API;
using System;
using System.Net;
using System.IO;

namespace currency_converter
{
    public class DataAccess
    {
        SQLiteConnection dbConn;
        public static object locker = new object();
        public static DataAccess db;

        public static DataAccess GetDB
        {
            get
            {
                if (db == null)
                {
                    db = new DataAccess();
                }
                return db;
            }
        }

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
            if (!TableExists("CurrencyModel"))
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
            lock (locker)
            {
                return dbConn.Query<CurrencyModel>("Select * From [CurrencyModel]");
            }
        }
        public int SaveCurrency(CurrencyModel aCurrency)
        {
            lock (locker)
            {
                return dbConn.Insert(aCurrency);
            }
        }

        public int UpdateCurrency(CurrencyModel aCurrency)
        {
            lock (locker)
            {
                Debug.WriteLine("Entrei no update");

                return dbConn.Update(aCurrency);
            }
        }

        public CurrencyModel GetCurrency(string code)
        {
            lock (locker)
            {
                return dbConn.Table<CurrencyModel>().FirstOrDefault(currency => currency.code == code);
            }
        }

        public int DeleteCurrency(CurrencyModel aCurrency)
        {
            lock (locker)
            {
                return dbConn.Delete(aCurrency);
            }
        }

        public List<WalletModel> GetWalletEntries()
        {
            return dbConn.Query<WalletModel>("Select * From [WalletModel]");
        }

        public int SaveWalletEntry(WalletModel aWalletEntry)
        {
            lock (locker)
            {
                return dbConn.Insert(aWalletEntry);
            }
        }
        public int DeleteWalletEntry(WalletModel aWalletEntry)
        {
            lock (locker)
            {
                return dbConn.Delete(aWalletEntry);
            }
        }

        public int UpdateWalletEntry(WalletModel aWalletEntry)
        {
            lock (locker)
            {
                return dbConn.Update(aWalletEntry);
            }
        }

        public WalletModel GetWalletEntry(string code)
        {
            lock (locker)
            {
                return dbConn.Table<WalletModel>().FirstOrDefault(walletAmount => walletAmount.code == code);
            }
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

        public void printListOfCurrencies()
        {
            List<CurrencyModel> dbContent = new List<CurrencyModel>();
            dbContent = DataAccess.GetDB.GetAllCurrency();

            for (int i = 0; i < dbContent.Count; i++)
            {
                Debug.WriteLine("123Currency code: " + dbContent[i].code + " Currency name: " + dbContent[i].name + " Currency Value: " + dbContent[i].toCurrency);
            }
        }

        public void printWallet()
        {
            List<WalletModel> dbContent = new List<WalletModel>();
            dbContent = DataAccess.GetDB.GetWalletEntries();

            for (int i = 0; i < dbContent.Count; i++)
            {
                Debug.WriteLine("Wallet -> Code: " + dbContent[i].code + " Amount: " + dbContent[i].amount);
            }
        }
    }
}