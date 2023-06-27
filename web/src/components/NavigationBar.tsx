import { Disclosure, Menu} from "@headlessui/react";
import Logo from "./Logo.tsx";
import { Link, useLocation } from "react-router-dom";


const navigation = [
    { name: "Categories", href: "/categories"},
    { name: "Goods", href: "/goods"},
];

function classNames(...classes: string[]): string {
    return classes.filter(Boolean).join(" ");
}

export default function NavigationBar() {
    const location = useLocation();
        return (

            <Disclosure as="nav">
                <>
                    <div className="max-w-7xl mx-auto px-2 sm:px-6 lg:px-8">
                        <div className="relative flex items-center justify-between h-16">
                            <div className="absolute inset-y-0 left-0 flex items-center sm:hidden">
                                <Disclosure.Button
                                    className="inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-white hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white">
                                    <span className="sr-only">Open main menu</span>

                                </Disclosure.Button>
                            </div>
                            <div className="flex-1 flex items-center justify-center sm:items-stretch sm:justify-start">
                                <div className="flex-shrink-0 flex items-center">
                                    <Logo/>

                                </div>
                                <div className="hidden sm:block sm:ml-6">
                                    <div className="flex space-x-4">
                                        {navigation.map((item) => (
                                            <Link
                                                key={item.name}
                                                to={item.href}
                                                className={classNames(
                                                    location.pathname === item.href
                                                        ? "bg-gray-900 text-white"
                                                        : "text-gray-300 hover:bg-gray-700 hover:text-white",
                                                    "px-3 py-2 rounded-md text-sm font-medium"
                                                )}
                                                aria-current={
                                                    location.pathname === item.href ? "page" : undefined
                                                }
                                            >
                                                {item.name}
                                            </Link>
                                        ))}
                                    </div>
                                </div>
                            </div>
                            <div
                                className="absolute inset-y-0 right-0 flex items-center pr-2 sm:static sm:inset-auto sm:ml-6 sm:pr-0">
                                <Menu as="div" className="ml-3 relative">
                                    <div>
                                        <Menu.Button>
                                            <span className="sr-only">Open user menu</span>
                                            <button
                                                className="w-104 h-47 flex-shrink-0 flex items-center justify-center bg-green-500 hover:bg-green-600 text-white"
                                                title="Sign In"
                                                style={{
                                                    borderRadius: '15px',
                                                    background: '#32B260',
                                                    color: '#FFF',
                                                    fontSize: '20px',
                                                    fontFamily: 'Hind Madurai',
                                                    padding: '5px 10px'
                                                }}
                                            >
                                                Sign In
                                            </button>
                                        </Menu.Button>
                                    </div>
                                </Menu>
                            </div>


                        </div>
                    </div>

                    <Disclosure.Panel className="sm:hidden">
                        <div className="px-2 pt-2 pb-3 space-y-1">
                            {navigation.map((item) => (
                                <Link
                                    key={item.name}
                                    to={item.href}
                                    className={classNames(
                                        location.pathname === item.href
                                            ? "bg-gray-900 text-white"
                                            : "text-gray-300 hover:bg-gray-700 hover:text-white",
                                        "block px-3 py-2 rounded-md text-base font-medium"
                                    )}
                                    aria-current={location.pathname === item.href ? "page" : undefined}
                                >
                                    {item.name}
                                </Link>
                            ))}
                        </div>
                    </Disclosure.Panel>

                </>

            </Disclosure>

        );
}