using System.Diagnostics;
using System.Collections.Generic;
using Xamarin.Forms;
using currency_converter.model;
using System;

namespace currency_converter
{
    public partial class AddRemoveCurrencyPage : ContentPage
    {
        enum Operation { Add, Remove };

        Dictionary<string, Operation> operations = new Dictionary<string, Operation>
        {
            { "Add", Operation.Add }, { "Remove", Operation.Remove }
        };

        Dictionary<string, CurrencyModel> currencies = new Dictionary<string, CurrencyModel>();

        Picker operationPicker, currenciesPicker;
        Entry amountEntry;
        Label currencyValueLabel;
        Label errorLabel;

        public AddRemoveCurrencyPage()
        {
            List<CurrencyModel> dbContent = new List<CurrencyModel>();
            dbContent = DataAccess.GetDB.GetAllCurrency();

            for (int i = 0; i < dbContent.Count; i++)
            {
                Debug.WriteLine("Lista: Currency code: " + dbContent[i].code + " Currency name: " + dbContent[i].name + " Currency Value: " + dbContent[i].toCurrency);
            }

            for (int i = 0; i < dbContent.Count; i++)
            {
                currencies.Add(dbContent[i].code, dbContent[i]);
            }

            Title = "Add/Remove Currency";

            errorLabel = new Label()
            {
                Text = "",
                FontSize = Device.GetNamedSize(NamedSize.Small, typeof(Label)),
                TextColor = Color.Red,
                HorizontalTextAlignment = TextAlignment.Center,
                VerticalOptions = LayoutOptions.Center,
                HorizontalOptions = LayoutOptions.Center,
                WidthRequest = 250
            };

            Label operation = new Label();
            Device.OnPlatform(
                Android: () => operation = new Label()
                {
                    Text = "Operation",
                    FontSize = Device.GetNamedSize(NamedSize.Large, typeof(Label)),
                    TextColor = Color.FromHex("#3498DB"),
                    VerticalOptions = LayoutOptions.Center,
                    HorizontalOptions = LayoutOptions.Center,
                    WidthRequest = 250
                },
                WinPhone: () => operation = new Label()
                {
                    Text = "Operation",
                    TextColor = Color.Black,
                    FontSize = Device.GetNamedSize(NamedSize.Large, typeof(Label)),
                    VerticalOptions = LayoutOptions.Center,
                    HorizontalOptions = LayoutOptions.Center,
                    WidthRequest = 250
                }
            );

            operationPicker = new Picker();
            Device.OnPlatform(
                Android: () => operationPicker = new Picker()
                {
                    Title = "Select",
                    WidthRequest = 250,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center
                },
                WinPhone: () => operationPicker = new Picker()
                {
                    Title = "Select",
                    WidthRequest = 250,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center,
                }
            );

            foreach (string operationName in operations.Keys)
            {
                operationPicker.Items.Add(operationName);
            }

            Label amountLabel = new Label();
            Device.OnPlatform(
                Android: () => amountLabel = new Label()
                {
                    Text = "Amount",
                    FontSize = Device.GetNamedSize(NamedSize.Large, typeof(Label)),
                    TextColor = Color.FromHex("#3498DB"),
                    VerticalOptions = LayoutOptions.Center,
                    HorizontalOptions = LayoutOptions.Center,
                    WidthRequest = 250
                },
                WinPhone: () => amountLabel = new Label()
                {
                    Text = "Amount",
                    FontSize = Device.GetNamedSize(NamedSize.Large, typeof(Label)),
                    TextColor = Color.Black,
                    VerticalOptions = LayoutOptions.Center,
                    HorizontalOptions = LayoutOptions.Center,
                    WidthRequest = 250
                }
            );

            amountEntry = new Entry();
            Device.OnPlatform(
                Android: () => amountEntry = new Entry()
                {
                    Placeholder = "Amount Value",
                    FontSize = Device.GetNamedSize(NamedSize.Medium, typeof(Entry)),
                    TextColor = Color.Black,
                    VerticalOptions = LayoutOptions.Center,
                    HorizontalOptions = LayoutOptions.Center,
                    WidthRequest = 250,
                    Keyboard = Keyboard.Numeric
                },
                WinPhone: () => amountEntry = new Entry()
                {
                    Placeholder = "Amount Value",
                    FontSize = Device.GetNamedSize(NamedSize.Medium, typeof(Entry)),
                    TextColor = Color.Black,
                    VerticalOptions = LayoutOptions.Center,
                    HorizontalOptions = LayoutOptions.Center,
                    WidthRequest = 250,
                    Keyboard = Keyboard.Numeric
                }
            );

            currenciesPicker = new Picker();
            Device.OnPlatform(
                Android: () => currenciesPicker = new Picker()
                {
                    Title = "Currency",
                    WidthRequest = 250,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center,
                },
                WinPhone: () => currenciesPicker = new Picker()
                {
                    Title = "Currency",
                    WidthRequest = 250,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center
                }
            );

            foreach (string currencyName in currencies.Keys)
            {
                currenciesPicker.Items.Add(currencyName);
            }

            operationPicker.SelectedIndexChanged += (sender, args) =>
            {
                if (operationPicker.SelectedIndex != -1)
                {
                    Device.OnPlatform(
                        Android: () => operationPicker.BackgroundColor = Color.Transparent,
                        WinPhone: () => operationPicker.BackgroundColor = Color.Transparent
                    );
                }
            };

            amountEntry.TextChanged += (sender, args) =>
            {
                currencyValueLabel.Text = "";
                errorLabel.Text = "";

                if (amountEntry.Text.Length != 0 && amountEntry.Text != null)
                {
                    Device.OnPlatform(
                        Android: () => amountEntry.BackgroundColor = Color.Transparent,
                        WinPhone: () => amountEntry.BackgroundColor = Color.Transparent
                    );
                }
            };

            currenciesPicker.SelectedIndexChanged += (sender, args) =>
            {
                currencyValueLabel.Text = "";
                errorLabel.Text = "";

                if (currenciesPicker.SelectedIndex != -1)
                {
                    Device.OnPlatform(
                        Android: () => currenciesPicker.BackgroundColor = Color.Transparent,
                        WinPhone: () => currenciesPicker.BackgroundColor = Color.Transparent
                    );
                }

                if (currenciesPicker.SelectedIndex != -1 && amountEntry.Text != null && amountEntry.Text.Length != 0)
                {
                    CurrencyModel c = DataAccess.GetDB.GetCurrency(currenciesPicker.Items[currenciesPicker.SelectedIndex]);
                    currencyValueLabel.Text = "1 EUR = " + c.toCurrency + " " + currenciesPicker.Items[currenciesPicker.SelectedIndex];
                }
            };

            currencyValueLabel = new Label();
            Device.OnPlatform(
                Android: () => currencyValueLabel = new Label()
                {
                    Text = "",
                    FontSize = Device.GetNamedSize(NamedSize.Small, typeof(Label)),
                    TextColor = Color.FromHex("#3498DB"),
                    VerticalOptions = LayoutOptions.Center,
                    HorizontalOptions = LayoutOptions.Center,
                    WidthRequest = 250
                },
                WinPhone: () => currencyValueLabel = new Label()
                {
                    Text = "",
                    FontSize = Device.GetNamedSize(NamedSize.Small, typeof(Label)),
                    TextColor = Color.Black,
                    VerticalOptions = LayoutOptions.Center,
                    HorizontalOptions = LayoutOptions.Center,
                    WidthRequest = 250
                }
            );

            Button confirm_btn = new Button();
            Device.OnPlatform(
                Android: () => confirm_btn = new Button()
                {
                    Text = "Confirm",
                    Font = Font.SystemFontOfSize(NamedSize.Medium),
                    BorderWidth = 1,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center,
                    BackgroundColor = Color.FromHex("#3498DB"),
                    TextColor = Color.White,
                    WidthRequest = 250
                },
                WinPhone: () => confirm_btn = new Button()
                {
                    Text = "Confirm",
                    Font = Font.SystemFontOfSize(NamedSize.Medium),
                    BorderWidth = 1,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center,
                    BackgroundColor = Color.White,
                    TextColor = Color.Black,
                    WidthRequest = 250
                }
            );
            confirm_btn.Clicked += OnConfirmButtonClicked;

            ToolbarItem walletToolbarItem = new ToolbarItem();
            Device.OnPlatform(
                Android: () => walletToolbarItem = new ToolbarItem()
                {
                    Text = "Wallet",
                    Order = ToolbarItemOrder.Primary
                },
                WinPhone: () => walletToolbarItem = new ToolbarItem()
                {
                    Text = "Confirm"
                }
            );
            walletToolbarItem.Clicked += OnClickedWalletToolbarItem;

            ToolbarItems.Add(walletToolbarItem);

            Content = new StackLayout
            {
                Children =
                {
                    new StackLayout
                    {
                        VerticalOptions = LayoutOptions.CenterAndExpand,
                        Children =
                        {
                            errorLabel,
                            operation,
                            operationPicker,
                            amountLabel,
                            amountEntry,
                            currenciesPicker,
                            currencyValueLabel,
                            confirm_btn
                        }
                    }
                }
            };
        }

        private bool CheckValues()
        {
            if (operationPicker.SelectedIndex == -1 && (amountEntry.Text == null || amountEntry.Text.Length == 0) && currenciesPicker.SelectedIndex == -1)
            {
                operationPicker.BackgroundColor = Color.Red;
                amountEntry.BackgroundColor = Color.Red;
                currenciesPicker.BackgroundColor = Color.Red;

                errorLabel.Text = "Missing operation, amount and currency!";

                return false;

            }
            else if (operationPicker.SelectedIndex == -1 && (amountEntry.Text == null || amountEntry.Text.Length == 0)) {
                operationPicker.BackgroundColor = Color.Red;
                amountEntry.BackgroundColor = Color.Red;

                errorLabel.Text = "Missing operation and amount!";

                return false;
            }
            else if ((amountEntry.Text == null || amountEntry.Text.Length == 0) && currenciesPicker.SelectedIndex == -1)
            {
                amountEntry.BackgroundColor = Color.Red;
                currenciesPicker.BackgroundColor = Color.Red;

                errorLabel.Text = "Missing amount and currency!";

                return false;
            }
            else if (operationPicker.SelectedIndex == -1 && currenciesPicker.SelectedIndex == -1)
            {
                operationPicker.BackgroundColor = Color.Red;
                currenciesPicker.BackgroundColor = Color.Red;

                errorLabel.Text = "Missing operation and currency!";

                return false;
            }
            else if (operationPicker.SelectedIndex == -1)
            {
                operationPicker.BackgroundColor = Color.Red;

                errorLabel.Text = "Missing operation!";

                return false;
            }
            else if (amountEntry.Text == null || amountEntry.Text.Length == 0)
            {
                amountEntry.BackgroundColor = Color.Red;

                errorLabel.Text = "Missing amount!";

                return false;
            } 
            else if (currenciesPicker.SelectedIndex == -1)
            {
                currenciesPicker.BackgroundColor = Color.Red;

                errorLabel.Text = "Missing currency!";

                return false;
            }

            return true;
        }

        void OnConfirmButtonClicked(object sender, EventArgs e)
        {
            if(CheckValues())
            {
                string typeOfOperation = operationPicker.Items[operationPicker.SelectedIndex];
                double amount = double.Parse(amountEntry.Text);
                string code = currenciesPicker.Items[currenciesPicker.SelectedIndex];

                switch (typeOfOperation)
                {
                    case "Add":
                        Debug.WriteLine("Operation: " + typeOfOperation + " Amount: " + amount + " Code: " + code);
                        AddWalletEntry(code, amount);
                        break;
                    case "Remove":
                        Debug.WriteLine("Operation: " + typeOfOperation + " Amount: " + amount + " Code: " + code);
                        RemoveWalletEntry(code, amount);
                        break;
                }

                ClearInputValues();

                Debug.WriteLine("Resposta: " + currencyValueLabel.Text);

                /*WalletModel w = new WalletModel()
                {
                    code = currenciesPicker.Items[currenciesPicker.SelectedIndex],
                    amount = double.Parse(amountEntry.Text)
                };
                DataAccess.GetDB.SaveWalletEntry(w);

                DataAccess.GetDB.printWallet();*/
            }
        }
        
        async void OnClickedWalletToolbarItem(object sender, EventArgs e)
        {
            WalletPage newPage = new WalletPage();
            await Navigation.PushAsync(newPage, true);
        }

        private void AddWalletEntry(string code, double amount)
        {
            DataAccess db = DataAccess.GetDB;

            WalletModel w = db.GetWalletEntry(code);

            if (w == null)
            {
                Debug.WriteLine("Wallet Amount Nulo");

                w = new WalletModel()
                {
                    code = currenciesPicker.Items[currenciesPicker.SelectedIndex],
                    amount = double.Parse(amountEntry.Text)
                };

                db.SaveWalletEntry(w);
            }
            else
            {
                Debug.WriteLine("Wallet Amount Existe");

                w.amount += amount;

                db.UpdateWalletEntry(w);
            }

            db.printWallet();
        }

        private void RemoveWalletEntry(string code, double amount)
        {
            DataAccess db = DataAccess.GetDB;

            WalletModel w = db.GetWalletEntry(code);

            if (w == null)
            {
                // TODO invocar label de erro antes de tudo para avisar que a wallet que pretende apagar nao existe
            }
            else
            {
                if(w.amount > amount)
                {
                    w.amount -= amount;

                    db.UpdateWalletEntry(w);
                }
                else
                {
                    db.DeleteWalletEntry(w);
                }
            }

            db.printWallet();
        }

        private void ClearInputValues()
        {
            operationPicker.SelectedIndex = -1;
            currenciesPicker.SelectedIndex = -1;
            amountEntry.Text = "";
        }
    }
}
