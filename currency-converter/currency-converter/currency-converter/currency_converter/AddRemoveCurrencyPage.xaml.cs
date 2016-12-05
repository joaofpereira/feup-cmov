using System.Diagnostics;
using System.Collections.Generic;
using Xamarin.Forms;
using currency_converter.model;

namespace currency_converter
{
    public partial class AddRemoveCurrencyPage : ContentPage
    {
        enum Operation { Add, Remove};

        Dictionary<string, Operation> operations = new Dictionary<string, Operation>
        {
            { "Add", Operation.Add }, { "Remove", Operation.Remove }
        };

        public AddRemoveCurrencyPage()
        {
            List<CurrencyModel> dbContent = new List<CurrencyModel>();
            dbContent = App.dbUtils.GetAllCurrencyNames();
            for (int i = 0; i < dbContent.Count; i++)
            {
                Debug.WriteLine("Currency code: " + dbContent[i].code);
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
                    TextColor = Color.White,
                    FontSize = Device.GetNamedSize(NamedSize.Large, typeof(Label)),
                    VerticalOptions = LayoutOptions.Center,
                    HorizontalOptions = LayoutOptions.Center,
                    WidthRequest = 250
                },
                Default: () => operation = new Label()
                {
                    Text = "Operation",
                    TextColor = Color.Black,
                    FontSize = Device.GetNamedSize(NamedSize.Large, typeof(Label)),
                    VerticalOptions = LayoutOptions.Center,
                    HorizontalOptions = LayoutOptions.Center,
                    WidthRequest = 250
                }
            );

            Picker operationPicker = new Picker();
            Device.OnPlatform(
                Android: () => operationPicker = new Picker()
                {
                    Title = "Select",
                    WidthRequest = 250,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center,
                },
                WinPhone: () => operationPicker = new Picker()
                {
                    WidthRequest = 250,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center
                },
                Default: () => operationPicker = new Picker()
                {
                    WidthRequest = 250,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center
                }
            );

            foreach (string operationName in operations.Keys)
            {
                operationPicker.Items.Add(operationName);
            }

            /*picker.SelectedIndexChanged += (sender, args) =>
            {
                if (picker.SelectedIndex == -1)
                {
                    boxView.Color = Color.Default;
                }
                else
                {
                    string colorName = picker.Items[picker.SelectedIndex];
                    boxView.Color = nameToColor[colorName];
                }
            };*/


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
                    TextColor = Color.White,
                    VerticalOptions = LayoutOptions.Center,
                    HorizontalOptions = LayoutOptions.Center,
                    WidthRequest = 250
                },
                Default: () => amountLabel = new Label()
                {
                    Text = "Amount",
                    FontSize = Device.GetNamedSize(NamedSize.Large, typeof(Label)),
                    TextColor = Color.Yellow,
                    VerticalOptions = LayoutOptions.Center,
                    HorizontalOptions = LayoutOptions.Center,
                    WidthRequest = 250
                }
            );

            Entry amountEntry = new Entry();
            Device.OnPlatform(
                Android: () => amountEntry = new Entry()
                {
                    Placeholder = "Amount Value",
                    FontSize = Device.GetNamedSize(NamedSize.Medium, typeof(Entry)),
                    TextColor = Color.FromHex("#3498DB"),
                    VerticalOptions = LayoutOptions.Center,
                    HorizontalOptions = LayoutOptions.Center,
                    WidthRequest = 250,
                    Keyboard = Keyboard.Numeric
                },
                WinPhone: () => amountEntry = new Entry()
                {
                    Placeholder = "Amount Value",
                    FontSize = Device.GetNamedSize(NamedSize.Medium, typeof(Entry)),
                    TextColor = Color.White,
                    BackgroundColor = Color.Black,
                    VerticalOptions = LayoutOptions.Center,
                    HorizontalOptions = LayoutOptions.Center,
                    WidthRequest = 250,
                    Keyboard = Keyboard.Numeric
                },
                Default: () => amountEntry = new Entry()
                {
                    Placeholder = "Amount Value",
                    FontSize = Device.GetNamedSize(NamedSize.Medium, typeof(Entry)),
                    TextColor = Color.Yellow,
                    VerticalOptions = LayoutOptions.Center,
                    HorizontalOptions = LayoutOptions.Center,
                    WidthRequest = 250,
                    Keyboard = Keyboard.Numeric
                }
            );

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
                            amountEntry
                        }
                    }
                }
            };
        }
    }
}
