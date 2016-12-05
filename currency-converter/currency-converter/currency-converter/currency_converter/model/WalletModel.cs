using SQLite;

namespace currency_converter.model
{
    public class WalletModel
    {
        [PrimaryKey, AutoIncrement]
        public long id { get; set; }
        [NotNull]
        public string code { get; set; }
        [NotNull]
        public string name { get; set; }
        [NotNull]
        public float amount { get; set; }
       
    }
}