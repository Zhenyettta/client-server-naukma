
export default function Categories() {
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
                    >
                        Add category
                    </button>
            </div>
        </div>


    );
}