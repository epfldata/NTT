globals [
    test
    index
]

breed [ wolves wolf ]

turtles-own [
    test
    test3-test?
    test4
    other
]
wolves-own [
    test2
    test5-test?
]

to go
    let m2 5
    let m 2.0
    ask turtles [
        ask myself [
            right m2
        ]
        right m2
    ]
end

to argtest [ value ]
    let m 5
    set m 0.0
    ifelse value = 0.0 [
        right 5
    ]
    value > 2.0
    [
        left 5
    ]
    [
        fw 5
    ]
end

to arg
    let m 5.0
    set m 0
    right 0
    fw 1
    argtest 5.0
    argtest 5
    show "test"
end