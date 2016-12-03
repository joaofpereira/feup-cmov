using System;
using Xamarin.Forms;

namespace currency_converter
{
    public partial class MainPage : ContentPage
    {
        public MainPage()
        {
            Label header = new Label
            {
                Text = "Currency Converter",
                Font = Font.BoldSystemFontOfSize(30),
                HorizontalOptions = LayoutOptions.Center
            };

            Button add_currency_btn = new Button
            {
                Text = "Add Currency",
                Font = Font.SystemFontOfSize(NamedSize.Large),
                BorderWidth = 1,
                HorizontalOptions = LayoutOptions.Center,
                VerticalOptions = LayoutOptions.Center,
                BackgroundColor = Color.FromHex("#00BFFF"),
                Margin = new Thickness(0, 20, 0, 20)
            };
            add_currency_btn.Clicked += OnAddCurrencyButtonClicked;

            Button wallet_btn = new Button
            {
                Text = "View Wallet",
                Font = Font.SystemFontOfSize(NamedSize.Large),
                BorderWidth = 1,
                HorizontalOptions = LayoutOptions.Center,
                VerticalOptions = LayoutOptions.Center,
                BackgroundColor = Color.FromHex("#00BFFF"),
                Margin = new Thickness(0, 20, 0, 20)
            };
            wallet_btn.Clicked += OnWalletButtonClicked;

            BoxView divider = new BoxView();
            Device.OnPlatform(
                Android: () => divider = new BoxView() { Color = Color.Black, WidthRequest = 100, HeightRequest = 2 },
                WinPhone: () => divider = new BoxView() { Color = Color.White, WidthRequest = 100, HeightRequest = 2 }
            );

            // Build the page.
            this.Content = new StackLayout
            {
                Padding = new Thickness(5, Device.OnPlatform(20, 0, 0)),
                Children =
                {
                    header,
                    add_currency_btn,
                    divider,
                    wallet_btn
                }
            };
        }

        void OnAddCurrencyButtonClicked(object sender, EventArgs e)
        {

        }

        void OnWalletButtonClicked(object sender, EventArgs e)
        {

        }
    }
}
