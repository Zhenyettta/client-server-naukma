
export default function Categories() {
    const handleAddCategory = () => {
        const formWindow = window.open('', '_blank');
        formWindow.document.write(`
            <html>
            <head>
                <title>Add Category</title>
                <style>
                    body {
                        font-family: 'Montserrat';
                    }
                    .form-container {
                        max-width: 400px;
                        margin: 20px auto;
                        padding: 20px;
                        border: 1px solid #ccc;
                        border-radius: 10px;
                    }
                    .form-label {
                        display: block;
                        margin-bottom: 10px;
                        font-size: 16px;
                    }
                    .form-input {
                        width: 100%;
                        padding: 8px;
                        font-size: 16px;
                    }
                    .form-button {
                        margin-top: 20px;
                        padding: 10px 20px;
                        font-size: 16px;
                        background: #3E7FFF;
                        color: #FFF;
                        border: none;
                        border-radius: 10px;
                        cursor: pointer;
                    }
                </style>
            </head>
            <body>
                <div class="form-container">
                    <h2>Add Category</h2>
                    <form>
                        <label class="form-label" for="category-name">Category Name:</label>
                        <input class="form-input" type="text" id="category-name" name="category-name" required>
                        
                        <button class="form-button" type="submit">Submit</button>
                    </form>
                </div>
            </body>
            </html>
        `);
    };

    return (
        <div className="max-w-7xl mx-auto px-2 sm:px-6 lg:px-8">
            <div className="relative flex items-center justify-between h-16">
                <h1 className="text-black font-sans text-3xl">Categories</h1>
            </div>
            <div>
                <span className="sr-only">Add category</span>
                <button
                    className="w-104 h-47 flex-shrink-0 flex items-center justify-center bg-blue-600 hover:bg-blue-600 text-white"
                    title="Sign In"
                    style={{
                        borderRadius: '10px',
                        background: '#3E7FFF',
                        color: '#FFF',
                        fontSize: '20px',
                        fontFamily: 'Montserrat',
                        padding: '5px 10px'
                    }}
                    onClick={handleAddCategory}
                >
                    Add category
                </button>
            </div>
        </div>
    );
}
