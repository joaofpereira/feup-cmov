using SQLite;

namespace currency_converter.model
{
    public class WalletModel
    {
        [PrimaryKey, NotNull]
        public string code { get; set; }
        [NotNull]
        public double amount { get; set; }
       
    }
}