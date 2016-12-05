using currency_converter.model;
using System.IO;
using Windows.Storage;
using Xamarin.Forms;

[assembly: Dependency (typeof (SQLite_UWP))]
public class SQLite_UWP : ISQLite
{
    public SQLite_UWP() { }
    public SQLite.SQLiteConnection GetConnection()
    {
        var sqliteFilename = "wallet.db3";
        string path = Path.Combine(ApplicationData.Current.LocalFolder.Path, sqliteFilename);
        var conn = new SQLite.SQLiteConnection(path);
        return conn;
    }
}
