using System.Diagnostics;
using System.Collections.Generic;
using Xamarin.Forms;
using currency_converter.model;
using System;
using System.Net;
using System.IO;

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

        public AddRemoveCurrencyPage()
        {
            List<CurrencyModel> dbContent = new List<CurrencyModel>();
            dbContent = App.dbUtils.GetAllCurrency();
            for (int i = 0; i < dbContent.Count; i++)
            {
                currencies.Add(dbContent[i].code, dbContent[i]);
            }

            Title = "Add/Remove Currency";

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
                        WinPhone: () => operationPicker.BackgroundColor = Color.Transparent,
                        Default: () => operationPicker.BackgroundColor = Color.Transparent
                    );
                }
            };

            amountEntry.TextChanged += (sender, args) =>
            {
                currencyValueLabel.Text = "";

                if (amountEntry.Text.Length != 0 && amountEntry.Text != null)
                {
                    Device.OnPlatform(
                        Android: () => amountEntry.BackgroundColor = Color.Transparent,
                        WinPhone: () => amountEntry.BackgroundColor = Color.Transparent,
                        Default: () => amountEntry.BackgroundColor = Color.Transparent
                    );
                }
            };

            currenciesPicker.SelectedIndexChanged += (sender, args) =>
            {
                if (currenciesPicker.SelectedIndex != -1)
                {
                    Device.OnPlatform(
                        Android: () => currenciesPicker.BackgroundColor = Color.Transparent,
                        WinPhone: () => currenciesPicker.BackgroundColor = Color.Transparent,
                        Default: () => currenciesPicker.BackgroundColor = Color.Transparent
                    );
                }

                if (amountEntry.Text != null && amountEntry.Text.Length != 0 && currenciesPicker.SelectedIndex != -1)
                {
                    //Debug.WriteLine("Valor: " + currenciesPicker.Items[currenciesPicker.SelectedIndex]);

                    var uri = string.Format("http://download.finance.yahoo.com/d/quotes?f=sl1d1t1&s=EUR{0}=X", currenciesPicker.Items[currenciesPicker.SelectedIndex]);
                    var cb = new AsyncCallback(CallHandler);
                    CallWebAsync(uri, currencyValueLabel, cb);
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

            this.Content = new StackLayout
            {
                Children =
                {
                    new StackLayout
                    {
                        VerticalOptions = LayoutOptions.CenterAndExpand,
                        Children =
                        {
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

                return false;

            }
            else if (operationPicker.SelectedIndex == -1 && (amountEntry.Text == null || amountEntry.Text.Length == 0)) {
                operationPicker.BackgroundColor = Color.Red;
                amountEntry.BackgroundColor = Color.Red;

                return false;
            }
            else if ((amountEntry.Text == null || amountEntry.Text.Length == 0) && currenciesPicker.SelectedIndex == -1)
            {
                amountEntry.BackgroundColor = Color.Red;
                currenciesPicker.BackgroundColor = Color.Red;

                return false;
            }
            else if (operationPicker.SelectedIndex == -1 && currenciesPicker.SelectedIndex == -1)
            {
                operationPicker.BackgroundColor = Color.Red;
                currenciesPicker.BackgroundColor = Color.Red;

                return false;
            }
            else if (operationPicker.SelectedIndex == -1)
            {
                operationPicker.BackgroundColor = Color.Red;

                return false;
            }
            else if (amountEntry.Text == null || amountEntry.Text.Length == 0)
            {
                amountEntry.BackgroundColor = Color.Red;

                return false;
            } 
            else if (currenciesPicker.SelectedIndex == -1)
            {
                currenciesPicker.BackgroundColor = Color.Red;
                return false;
            }

            return true;
        }

        void OnConfirmButtonClicked(object sender, EventArgs e)
        {
            if(CheckValues())
            {
                Debug.WriteLine("Resposta: " + currencyValueLabel.Text);
            }
        }

        private void CallWebAsync(string uri, Label response, AsyncCallback cb)
        {
            var request = HttpWebRequest.Create(uri);
            request.Method = "GET";
            var state = new Tuple<Label, WebRequest>(response, request);

            request.BeginGetResponse(cb, state);
        }

        private void CallHandler(IAsyncResult ar)
        {
            var state = (Tuple<Label, WebRequest>)ar.AsyncState;
            var request = state.Item2;

            using (HttpWebResponse response = request.EndGetResponse(ar) as HttpWebResponse)
            {
                if (response.StatusCode == HttpStatusCode.OK)
                    using (StreamReader reader = new StreamReader(response.GetResponseStream()))
                    {
                        var content = reader.ReadToEnd();

                        var tokens = content.Split(',');

                        Device.BeginInvokeOnMainThread(() => state.Item1.Text = "1 EUR = " + tokens[1] + " " + currenciesPicker.Items[currenciesPicker.SelectedIndex]);
                    }
            }
        }
    }
}
