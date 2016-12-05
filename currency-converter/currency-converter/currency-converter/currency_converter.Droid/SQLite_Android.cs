using Xamarin.Forms;
using System.IO;
using currency_converter.Droid;
using currency_converter.model;

[assembly: Dependency(typeof(SQLite_Android))]
namespace currency_converter.Droid
{
    public class SQLite_Android : ISQLite
    {
        public SQLite_Android() { }

        #region ISQLite implementation
        public SQLite.SQLiteConnection GetConnection()
        {
            var sqliteFilename = "wallet.db3";
            string documentsPath = System.Environment.GetFolderPath(System.Environment.SpecialFolder.Personal); // Documents folder
            var path = Path.Combine(documentsPath, sqliteFilename);
            // Create the connection
            var conn = new SQLite.SQLiteConnection(path);
            // Return the database connection
            return conn;
        }
        #endregion
    }
}