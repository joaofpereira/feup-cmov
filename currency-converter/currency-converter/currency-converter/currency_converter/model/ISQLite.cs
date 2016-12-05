using SQLite;

namespace currency_converter.model
{
    public interface ISQLite
    {
        SQLiteConnection GetConnection();
    }
}