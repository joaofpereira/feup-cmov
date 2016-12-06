using System;
using Xamarin.Forms;

namespace currency_converter
{
    public partial class MainPage : ContentPage
    {
        public MainPage()
        {
            Title = "Currency Converter";

            Button add_currency_btn = new Button();
            Device.OnPlatform(
                Android: () => add_currency_btn = new Button()
                {
                    Text = "Add/Remove Currency",
                    Font = Font.SystemFontOfSize(NamedSize.Large),
                    BorderWidth = 1,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center,
                    BackgroundColor = Color.FromHex("#3498DB"),
                    TextColor = Color.White,
                    Margin = new Thickness(0, 20, 0, 20)
                },
                WinPhone: () => add_currency_btn = new Button()
                {
                    Text = "Add/Remove Currency",
                    Font = Font.SystemFontOfSize(NamedSize.Large),
                    BorderWidth = 1,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center,
                    BackgroundColor = Color.White,
                    TextColor = Color.Black,
                    Margin = new Thickness(0, 20, 0, 20)
                },
                Default: () => add_currency_btn = new Button()
                {
                    Text = "Add/Remove Currency",
                    Font = Font.SystemFontOfSize(NamedSize.Large),
                    BorderWidth = 1,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center
                }
            );
            add_currency_btn.Clicked += OnAddCurrencyButtonClicked;

            Button wallet_btn = new Button();
            Device.OnPlatform(
                Android: () => wallet_btn = new Button()
                {
                    Text = "View Wallet",
                    Font = Font.SystemFontOfSize(NamedSize.Large),
                    BorderWidth = 1,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center,
                    BackgroundColor = Color.FromHex("#3498DB"),
                    TextColor = Color.White,
                    Margin = new Thickness(0, 20, 0, 20)
                    
                },
                WinPhone: () => wallet_btn = new Button()
                {
                    Text = "View Wallet",
                    Font = Font.SystemFontOfSize(NamedSize.Large),
                    BorderWidth = 1,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center,
                    BackgroundColor = Color.White,
                    TextColor = Color.Black,
                    Margin = new Thickness(0, 20, 0, 20)
                },
                Default: () => wallet_btn = new Button()
                {
                    Text = "View Wallet",
                    Font = Font.SystemFontOfSize(NamedSize.Large),
                    BorderWidth = 1,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center
                }
            );
            wallet_btn.Clicked += OnWalletButtonClicked;

            BoxView divider = new BoxView();
            Device.OnPlatform(
                Android: () => {
                    divider = new BoxView() { Color = Color.FromHex("#3498DB"), WidthRequest = 100, HeightRequest = 2 };
                },
                WinPhone: () =>
                {
                    divider = new BoxView() { Color = Color.White, WidthRequest = 100, HeightRequest = 2 };
                }
            );

            // Build the page.
            this.Content = new StackLayout
            {
                Children =
                {
                    new StackLayout
                    {
                        VerticalOptions = LayoutOptions.CenterAndExpand,
                        Children =
                        {
                            add_currency_btn,
                            divider,
                            wallet_btn
                        }
                    }
                }
            };
        }

        async void OnAddCurrencyButtonClicked(object sender, EventArgs e)
        {
            AddRemoveCurrencyPage newPage = new AddRemoveCurrencyPage();
            await Navigation.PushAsync(newPage, true);
        }

        async void OnWalletButtonClicked(object sender, EventArgs e)
        {
            WalletPage newPage = new WalletPage();
            await Navigation.PushAsync(newPage, true);
        }
    }
}
