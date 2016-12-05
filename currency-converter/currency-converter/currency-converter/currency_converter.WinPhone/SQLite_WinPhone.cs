using Windows.Storage;
using currency_converter.model;
using Xamarin.Forms;
using System.IO;

[assembly: Dependency(typeof(SQLite_WinPhone))]
public class SQLite_WinPhone : ISQLite
{
    public SQLite_WinPhone () { }
    public SQLite.SQLiteConnection GetConnection()
    {
        var sqliteFilename = "wallet.db3";
        string path = Path.Combine(ApplicationData.Current.LocalFolder.Path, sqliteFilename);
        var conn = new SQLite.SQLiteConnection(path);
        return conn;
    }
}