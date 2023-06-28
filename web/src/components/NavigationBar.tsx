import { Disclosure, Menu } from "@headlessui/react";
import { Link, useLocation } from "react-router-dom";

const navigation = [
    { name: "Categories", href: "/goods/categories" },
    { name: "Goods", href: "/goods" },
];

function classNames(...classes: string[]): string {
    return classes.filter(Boolean).join(" ");
}

export default function NavigationBar() {
    const location = useLocation();

    return (
        <Disclosure as="nav" className="bg-lightblue-500">
            <>
                <div className="max-w-7xl mx-auto px-2 sm:px-6 lg:px-8">
                    <div className="relative flex items-center justify-between h-16">
                        <div className="absolute inset-y-0 left-0 flex items-center sm:hidden">
                            <Disclosure.Button
                                className="inline-flex items-center justify-center p-2 rounded-md text-black hover:text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white"
                            >
                                <span className="sr-only">Open main menu</span>
                            </Disclosure.Button>
                        </div>
                        <div className="flex items-center justify-start">
                            <div className="flex-shrink-0 flex items-center">
                                {/* Add your logo or branding here */}
                            </div>
                            <div className="hidden sm:block sm:ml-auto">
                                <div className="flex space-x-4">
                                    <div className="flex items-center ml-auto space-x-4">
                                        {navigation.map((item) => (
                                            <Link
                                                key={item.name}
                                                to={item.href}
                                                className={classNames(
                                                    location.pathname === item.href
                                                        ? "bg-blue-800 text-white"
                                                        : "text-black hover:bg-blue-700 hover:text-white",
                                                    "px-3 py-2 rounded-md text-base font-medium"
                                                )}
                                                aria-current={
                                                    location.pathname === item.href ? "page" : undefined
                                                }
                                                style={{
                                                    textDecoration: "none",
                                                    fontSize: "1.1rem",
                                                }}
                                            >
                                                {item.name}
                                            </Link>
                                        ))}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="absolute inset-y-0 right-0 flex items-center pr-2 sm:static sm:inset-auto sm:ml-6 sm:pr-0">
                            <Menu as="div" className="ml-3 relative">
                                <div>
                                    <Menu.Button>
                                        <span className="sr-only">Open user menu</span>
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
                                        ? "bg-blue-800 text-white"
                                        : "text-black hover:bg-blue-700 hover:text-white",
                                    "block px-3 py-2 rounded-md text-base font-medium"
                                )}
                                aria-current={location.pathname === item.href ? "page" : undefined}
                                style={{
                                    textDecoration: "none",
                                    fontSize: "1.1rem",
                                }}
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