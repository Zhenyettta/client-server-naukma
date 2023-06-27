/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      gradientColorStops: {
        'primary': '#E5F5F0',
        'secondary': '#FDEAE3',
      },
      colors: {
        'our-green': '#32B260',
      },
    },
  },
  plugins: [],
}

